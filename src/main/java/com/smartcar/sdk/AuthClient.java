package com.smartcar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.Auth;
import com.smartcar.sdk.data.RequestPaging;
import com.smartcar.sdk.data.ResponsePaging;
import com.smartcar.sdk.data.SmartcarResponse;
import com.smartcar.sdk.data.VehicleIds;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;

/**
 * Smartcar OAuth 2.0 Authentication Client
 */
public class AuthClient extends ApiClient {
  /**
   * Custom deserializer for Auth data from the OAuth endpoint.
   */
  private class AuthDeserializer implements JsonDeserializer<Auth> {
    /**
     * Deserializes the OAuth auth endpoint JSON into a new Auth object.
     *
     * @param json the Json data being deserialized
     * @param typeOfT the type of the Object to deserialize to
     * @param context the deserialization context
     *
     * @return the newly created Auth object
     */
    public Auth deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
      JsonObject jsonObject = json.getAsJsonObject();

      // Get timestamp for expiration.
      Calendar expiration = Calendar.getInstance();
      expiration.add(Calendar.SECOND, jsonObject.get("expires_in").getAsInt());

      Calendar refreshExpiration = Calendar.getInstance();
      refreshExpiration.add(Calendar.DAY, 60);

      return new Auth(
          jsonObject.get("access_token").getAsString(),
          jsonObject.get("refresh_token").getAsString(),
          expiration.getTime(),
          refreshExpiration.getTime()
      );
    }
  }

  private static final String URL_AUTHORIZE = "https://connect.smartcar.com/oauth/authorize";
  private static final String URL_ACCESS_TOKEN = "https://auth.smartcar.com/oauth/token";

  private String clientId;
  private String clientAuthorization;
  private String redirectUri;
  private String[] scope;
  private boolean development;
  public String urlAuthorize = AuthClient.URL_AUTHORIZE;
  public String urlAccessToken = AuthClient.URL_ACCESS_TOKEN;

  /**
   * Retrieves the user ID of the user authenticated with the specified access
   * token.
   *
   * @param accessToken a valid access token
   *
   * @return the corresponding user ID
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public static String getUserId(String accessToken) throws SmartcarException {
    // Build Request
    Request request = new Request.Builder()
        .url(HttpUrl.parse(AuthClient.urlApi + "/user"))
        .header("Authorization", "Bearer " + accessToken)
        .addHeader("User-Agent", AuthClient.USER_AGENT)
        .build();

    // Execute Request
    Response response = AuthClient.execute(request);

    // Parse Response
    JsonObject json;

    try {
      json = new Gson().fromJson(response.body().string(), JsonObject.class);
    } catch (IOException ex) {
      throw new SmartcarException(ex.getMessage());
    } catch (NullPointerException ex) {
      throw new SmartcarException("Received an empty response body.");
    }

    return json.get("id").getAsString();
  }

  /**
   * Retrieves all vehicle IDs associated with the authenticated user.
   *
   * @param accessToken a valid access token
   * @param paging paging parameters
   *
   * @return the requested vehicle IDs
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public static SmartcarResponse<VehicleIds> getVehicleIds(String accessToken, RequestPaging paging) throws SmartcarException {
    // Build Request
    HttpUrl url = HttpUrl.parse(AuthClient.urlApi + "/vehicles").newBuilder()
        .addQueryParameter("limit", String.valueOf(paging.getLimit()))
        .addQueryParameter("offset", String.valueOf(paging.getOffset()))
        .build();
    Request request = new Request.Builder()
        .url(url)
        .header("Authorization", "Bearer " + accessToken)
        .addHeader("User-Agent", AuthClient.USER_AGENT)
        .build();

    // Execute Request
    Response response = AuthClient.execute(request);

    // Parse Response
    JsonObject json = null;

    try {
      json = new Gson().fromJson(response.body().string(), JsonObject.class);
    } catch (IOException ex) {
      throw new SmartcarException(ex.getMessage());
    }

    JsonObject jsonPaging = json.get("paging").getAsJsonObject();
    ResponsePaging responsePaging = new ResponsePaging(jsonPaging.get("count").getAsInt(), jsonPaging.get("offset").getAsInt());
    JsonArray vehicles = json.get("vehicles").getAsJsonArray();
    int count = vehicles.size();
    String[] data = new String[count];

    for (int i = 0; i < count; i++) {
      data[i] = vehicles.get(i).getAsString();
    }

    return new SmartcarResponse<VehicleIds>(new VehicleIds(data), responsePaging);
  }

  /**
   * Initializes a new AuthClient.
   *
   * @param clientId the application client ID
   * @param clientSecret the application client secret
   * @param redirectUri the registered redirect URI for the application
   * @param scope the permission scope requested
   * @param development whether or not to operate in development mode
   */
  public AuthClient(String clientId, String clientSecret, String redirectUri, String[] scope, boolean development) {
    this.clientId = clientId;
    this.clientAuthorization = Credentials.basic(clientId, clientSecret);
    this.redirectUri = redirectUri;
    this.scope = scope;
    this.development = development;

    AuthClient.gson.registerTypeAdapter(Auth.class, new AuthDeserializer());
  }

  /**
   * Initializes a new AuthClient.
   *
   * @param clientId the application client ID
   * @param clientSecret the application client secret
   * @param redirectUri the registered redirect URI for the application
   * @param development whether or not to operate in development mode
   */
  public AuthClient(String clientId, String clientSecret, String redirectUri, boolean development) {
    this(clientId, clientSecret, redirectUri, null, development);
  }

  /**
   * Initializes a new AuthClient.
   *
   * @param clientId the application client ID
   * @param clientSecret the application client secret
   * @param redirectUri the registered redirect URI for the application
   * @param scope the permission scope requested
   */
  public AuthClient(String clientId, String clientSecret, String redirectUri, String[] scope) {
    this(clientId, clientSecret, redirectUri, scope, false);
  }

  /**
   * Initializes a new AuthClient.
   *
   * @param clientId the client ID to be used with all requests
   * @param clientSecret the client secret to be used with all requests
   * @param redirectUri the configured redirect URL associated with the account
   */
  public AuthClient(String clientId, String clientSecret, String redirectUri) {
    this(clientId, clientSecret, redirectUri, null, false);
  }

  /**
   * Executes an Auth API request.
   *
   * @param requestBody the request body to be included
   *
   * @return the parsed response
   *
   * @throws SmartcarException if the API request fails
   */
  private Auth call(RequestBody requestBody) throws SmartcarException {
    Request request = new Request.Builder()
        .url(this.urlAccessToken)
        .header("Authorization", this.clientAuthorization)
        .header("Content-Type", "application/json")
        .addHeader("User-Agent", AuthClient.USER_AGENT)
        .post(requestBody)
        .build();

    return AuthClient.execute(request, Auth.class).getData();
  }

  /**
   * Returns the assembled authentication URL.
   *
   * @param state an arbitrary string to be returned to the redirect URI
   * @param forcePrompt whether to force the approval prompt to show every auth
   *
   * @return the authentication URL
   */
  public String getAuthUrl(String state, boolean forcePrompt) {
    HttpUrl.Builder urlBuilder = HttpUrl.parse(this.urlAuthorize).newBuilder()
        .addQueryParameter("response_type", "code")
        .addQueryParameter("client_id", this.clientId)
        .addQueryParameter("redirect_uri", this.redirectUri)
        .addQueryParameter("approval_prompt", forcePrompt ? "force" : "auto");

    if(state != null) {
      urlBuilder.addQueryParameter("state", state);
    }

    if(this.scope != null) {
      urlBuilder.addQueryParameter("scope", Utils.join(this.scope, " "));
    }

    if(this.development) {
      urlBuilder.addQueryParameter("mock", "true");
    }

    return urlBuilder.build().toString();
  }

  /**
   * Returns the assembled authentication URL.
   *
   * @param state an arbitrary string to be returned to the redirect URI
   *
   * @return the authentication URL
   */
  public String getAuthUrl(String state) {
    return this.getAuthUrl(state, false);
  }

  /**
   * Returns the assembled authentication URL.
   *
   * @param forcePrompt whether to force the approval prompt to show every auth
   *
   * @return the authentication URL
   */
  public String getAuthUrl(boolean forcePrompt) {
    return this.getAuthUrl(null, forcePrompt);
  }

  /**
   * Returns the assembled authentication URL.
   *
   * @return the authentication URL
   */
  public String getAuthUrl() {
    return this.getAuthUrl(null);
  }

  /**
   * Exchanges an authorization code for an access token.
   *
   * @param code the authorization code
   *
   * @return the requested access token
   *
   * @throws SmartcarException when the request is unsuccessful
   */
  public Auth exchangeCode(String code) throws SmartcarException {
    RequestBody requestBody = new FormBody.Builder()
        .add("grant_type", "authorization_code")
        .add("code", code)
        .add("redirect_uri", this.redirectUri)
        .build();

    return this.call(requestBody);
  }

  /**
   * Exchanges a refresh token for a new access token.
   *
   * @param refreshToken the refresh token
   *
   * @return the requested access token
   *
   * @throws SmartcarException when the request is unsuccessful
   */
  public Auth exchangeRefreshToken(String refreshToken) throws SmartcarException {
    RequestBody requestBody = new FormBody.Builder()
        .add("grant_type", "authorization_code")
        .add("refresh_token", refreshToken)
        .build();

    return this.call(requestBody);
  }
}
