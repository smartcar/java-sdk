package com.smartcar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.Auth;
import com.smartcar.sdk.data.Compatibility;
import com.smartcar.sdk.data.RequestPaging;
import com.smartcar.sdk.data.ResponsePaging;
import com.smartcar.sdk.data.SmartcarResponse;
import com.smartcar.sdk.data.VehicleIds;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
      refreshExpiration.add(Calendar.DAY_OF_YEAR, 60);

      return new Auth(
          jsonObject.get("access_token").getAsString(),
          jsonObject.get("refresh_token").getAsString(),
          expiration.getTime(),
          refreshExpiration.getTime());
    }
  }

  private static final String URL_AUTHORIZE = "https://connect.smartcar.com/oauth/authorize";
  private static final String URL_ACCESS_TOKEN = "https://auth.smartcar.com/oauth/token";

  private String clientId;
  private String basicAuthorization;
  private String redirectUri;
  private String[] scope;
  private boolean testMode;
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
    Request request =
        new Request.Builder()
            .url(HttpUrl.parse(AuthClient.getApiUrl() + "/user"))
            .header("Authorization", "Bearer " + accessToken)
            .addHeader("User-Agent", AuthClient.USER_AGENT)
            .build();

    // Execute Request
    Response response = AuthClient.execute(request);

    // Parse Response
    JsonObject json;

    try {
      json = new Gson().fromJson(response.body().string(), JsonObject.class);
      return json.get("id").getAsString();
    } catch (IOException ex) {
      throw new SmartcarException(ex.getMessage());
    }
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
  public static SmartcarResponse<VehicleIds> getVehicleIds(String accessToken, RequestPaging paging)
      throws SmartcarException {
    // Build Request
    HttpUrl.Builder urlBuilder = HttpUrl.parse(AuthClient.getApiUrl() + "/vehicles").newBuilder();

    if (paging != null) {
      urlBuilder
          .addQueryParameter("limit", String.valueOf(paging.getLimit()))
          .addQueryParameter("offset", String.valueOf(paging.getOffset()));
    }

    HttpUrl url = urlBuilder.build();
    Request request =
        new Request.Builder()
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
    ResponsePaging responsePaging =
        new ResponsePaging(jsonPaging.get("count").getAsInt(), jsonPaging.get("offset").getAsInt());
    JsonArray vehicles = json.get("vehicles").getAsJsonArray();
    int count = vehicles.size();
    String[] data = new String[count];

    for (int i = 0; i < count; i++) {
      data[i] = vehicles.get(i).getAsString();
    }

    return new SmartcarResponse<VehicleIds>(new VehicleIds(data), responsePaging);
  }

  /**
   * Retrieves all vehicle IDs associated with the authenticated user.
   *
   * @param accessToken a valid access token
   *
   * @return the requested vehicle IDs
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public static SmartcarResponse<VehicleIds> getVehicleIds(String accessToken)
      throws SmartcarException {
    return AuthClient.getVehicleIds(accessToken, null);
  }

  /**
   * Convenience method for determining if an auth token expiration has passed.
   *
   * @param expiration the expiration date of the token
   *
   * @return whether or not the token has expired
   */
  public static boolean isExpired(Date expiration) {
    return !expiration.after(new Date());
  }

  /**
   * Initializes a new AuthClient.
   *
   * @param clientId the application client ID
   * @param clientSecret the application client secret
   * @param redirectUri the registered redirect URI for the application
   * @param scope the permission scope requested
   * @param testMode launch the Smartcar auth flow in test mode
   */
  public AuthClient(
      String clientId, String clientSecret, String redirectUri, String[] scope, boolean testMode) {
    this.clientId = clientId;
    this.basicAuthorization = Credentials.basic(clientId, clientSecret);
    this.redirectUri = redirectUri;
    this.scope = scope;
    this.testMode = testMode;

    AuthClient.gson.registerTypeAdapter(Auth.class, new AuthDeserializer());
  }

  /**
   * Initializes a new AuthClient.
   *
   * @param clientId the application client ID
   * @param clientSecret the application client secret
   * @param redirectUri the registered redirect URI for the application
   * @param testMode launch the Smartcar auth flow in test mode
   */
  public AuthClient(String clientId, String clientSecret, String redirectUri, boolean testMode) {
    this(clientId, clientSecret, redirectUri, null, testMode);
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
   * Creates an AuthUrlBuilder
   *
   * @return returns an instance of AuthUrlBuilder
   */
  public AuthUrlBuilder authUrlBuilder() {
    return new AuthUrlBuilder();
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
    Request request =
        new Request.Builder()
            .url(this.urlAccessToken)
            .header("Authorization", this.basicAuthorization)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("User-Agent", AuthClient.USER_AGENT)
            .post(requestBody)
            .build();

    return AuthClient.execute(request, Auth.class).getData();
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder} instead.
   *
   * A class that creates a custom AuthVehicleInfo object, which can be used
   * when generating an authentication URL.
   */
  @Deprecated
  public static class AuthVehicleInfo {
    private String make;

    /**
     * Assigns optional and required properties on the AuthVehicleInfo object.
     *
     * @param builder the builder to obtain the properties from
     */
    private AuthVehicleInfo(Builder builder) {
      this.make = builder.make;
    }

    /**
     * Returns the make assigned to AuthVehicleInfo
     *
     * @return the make of the vehicle
     */
    public String getMake() {
      return this.make;
    }

    /**
     * Builder class that allows for optional properties on AuthVehicleInfo
     */
    public static class Builder {
      private String make;

      /**
       * Sets the make on the Builder. Including a make allows the user to bypass the car brand
       * selection screen.
       *
       * @param make name of the make of a vehicle. For a list of supported makes, please see
       * <a href="https://smartcar.com/docs/api#request-authorization">our API Reference</a>
       *
       * @return the builder with a make property added
       */
      public Builder setMake(String make) {
        this.make = make;
        return this;
      }

      /**
       * Instantiates a new AuthVehicleInfo object, which will also have any optional properties
       * that are already set on the Builder object that is calling this method.
       *
       * @return a new instantiation of the AuthVehicleInfo class
       */
      public AuthVehicleInfo build() {
        return new AuthVehicleInfo(this);
      }
    }
  }

  /**
   * A builder for creating Authorization URLs. Access through {@link AuthClient#authUrlBuilder()}.
   */
  public class AuthUrlBuilder {
    private HttpUrl.Builder urlBuilder;

    public AuthUrlBuilder() {
      urlBuilder =
          HttpUrl.parse(AuthClient.this.urlAuthorize)
              .newBuilder()
              .addQueryParameter("response_type", "code")
              .addQueryParameter("client_id", AuthClient.this.clientId)
              .addQueryParameter("redirect_uri", AuthClient.this.redirectUri)
              .addQueryParameter("mode", AuthClient.this.testMode ? "test" : "live");
      if (AuthClient.this.scope.length != 0) {
        urlBuilder.addQueryParameter("scope", Utils.join(AuthClient.this.scope, " "));
      }
    }

    public AuthUrlBuilder setState(String state) {
      if (state != "") {
        urlBuilder.addQueryParameter("state", state);
      }
      return this;
    }

    public AuthUrlBuilder setApprovalPrompt(boolean approvalPrompt) {
      urlBuilder.addQueryParameter("approval_prompt", approvalPrompt ? "force" : "auto");
      return this;
    }

    public AuthUrlBuilder setMakeBypass(String make) {
      urlBuilder.addQueryParameter("make", make);
      return this;
    }

    public AuthUrlBuilder setSingleSelect(boolean singleSelect) {
      urlBuilder.addQueryParameter("single_select", Boolean.toString(singleSelect));
      return this;
    }

    public AuthUrlBuilder setSingleSelectVin(String vin) {
      urlBuilder.addQueryParameter("single_select_vin", vin);
      return this;
    }

    public AuthUrlBuilder setFlags(String[] flags) {
      urlBuilder.addQueryParameter("flags", Utils.join(flags, " "));
      return this;
    }

    public String build() {
      return urlBuilder.build().toString();
    }
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder}.
   *
   * Returns the assembled authentication URL.
   *
   * @param state an arbitrary string to be returned to the redirect URI
   * @param forcePrompt whether to force the approval prompt to show every auth
   * @param authVehicleInfo an optional AuthVehicleInfo object. Including the
   * make property causes the car brand selection screen to be bypassed.
   *
   * @return the authentication URL
   */
  @Deprecated
  public String getAuthUrl(String state, boolean forcePrompt, AuthVehicleInfo authVehicleInfo) {
    HttpUrl.Builder urlBuilder =
        HttpUrl.parse(this.urlAuthorize)
            .newBuilder()
            .addQueryParameter("response_type", "code")
            .addQueryParameter("client_id", this.clientId)
            .addQueryParameter("redirect_uri", this.redirectUri)
            .addQueryParameter("approval_prompt", forcePrompt ? "force" : "auto");

    if (state != null) {
      urlBuilder.addQueryParameter("state", state);
    }

    if (this.scope != null) {
      urlBuilder.addQueryParameter("scope", Utils.join(this.scope, " "));
    }

    if (this.testMode) {
      urlBuilder.addQueryParameter("mode", "test");
    } else {
      urlBuilder.addQueryParameter("mode", "live");
    }

    if (authVehicleInfo != null) {
      if (authVehicleInfo.getMake() != null) {
        urlBuilder.addQueryParameter("make", authVehicleInfo.getMake());
      }
    }

    return urlBuilder.build().toString();
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder}.
   *
   * Returns the assembled authentication URL.
   *
   * @param state an arbitrary string to be returned to the redirect URI
   *
   * @return the authentication URL
   */
  @Deprecated
  public String getAuthUrl(String state) {
    return this.getAuthUrl(state, false, null);
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder}.
   *
   * Returns the assembled authentication URL.
   *
   * @param forcePrompt whether to force the approval prompt to show every auth
   *
   * @return the authentication URL
   */
  @Deprecated
  public String getAuthUrl(boolean forcePrompt) {
    return this.getAuthUrl(null, forcePrompt, null);
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder}.
   *
   * Returns the assembled authentication URL.
   *
   * @param authVehicleInfo an optional AuthVehicleInfo object. Including the
   * make property causes the car brand selection screen to be bypassed.
   *
   * @return the authentication URL
   */
  @Deprecated
  public String getAuthUrl(AuthVehicleInfo authVehicleInfo) {
    return this.getAuthUrl(null, false, authVehicleInfo);
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder}.
   *
   * Returns the assembled authentication URL.
   *
   * @param state an arbitrary string to be returned to the redirect URI
   * @param authVehicleInfo an optional AuthVehicleInfo object. Including the
   * make property causes the car brand selection screen to be bypassed.
   *
   * @return the authentication URL
   */
  @Deprecated
  public String getAuthUrl(String state, AuthVehicleInfo authVehicleInfo) {
    return this.getAuthUrl(state, false, authVehicleInfo);
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder}.
   *
   * Returns the assembled authentication URL.
   *
   * @param forcePrompt whether to force the approval prompt to show every auth
   * @param authVehicleInfo an optional AuthVehicleInfo object. Including the
   * make property causes the car brand selection screen to be bypassed.
   *
   * @return the authentication URL
   */
  @Deprecated
  public String getAuthUrl(boolean forcePrompt, AuthVehicleInfo authVehicleInfo) {
    return this.getAuthUrl(null, forcePrompt, authVehicleInfo);
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder}.
   *
   * Returns the assembled authentication URL.
   *
   * @param state an arbitrary string to be returned to the redirect URI
   * @param forcePrompt whether to force the approval prompt to show every auth
   *
   * @return the authentication URL
   */
  @Deprecated
  public String getAuthUrl(String state, boolean forcePrompt) {
    return this.getAuthUrl(state, forcePrompt, null);
  }

  /**
   * @deprecated as of 2.1.0. Please use {@link AuthUrlBuilder}.
   *
   * Returns the assembled authentication URL.
   *
   * @return the authentication URL
   */
  @Deprecated
  public String getAuthUrl() {
    return this.getAuthUrl(null, false, null);
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
    RequestBody requestBody =
        new FormBody.Builder()
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
    RequestBody requestBody =
        new FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("refresh_token", refreshToken)
            .build();

    return this.call(requestBody);
  }

  /**
   * Determine if a vehicle is compatible with the Smartcar API and the provided permissions.
   * A compatible vehicle is a vehicle that:
   * <ol>
   * <li>
   * has the hardware required for internet connectivity,
   * </li>
   * <li>
   * belongs to the makes and models Smartcar supports, and
   * </li>
   * <li>
   * supports the permissions.
   * </li>
   * </ol>
   * @param vin the VIN (Vehicle Identification Number) of the vehicle.
   * @param scope An array of permissions. The valid permissions are found in the API Reference.
   *
   * @return false if the vehicle is not compatible. true if the vehicle is likely compatible.
   *
   * @throws SmartcarException when the request is unsuccessful
   */
  public boolean isCompatible(String vin, String[] scope) throws SmartcarException {
    return isCompatible(vin, scope, "US");
  }

  /**
   * Determine if a vehicle is compatible with the Smartcar API and the provided permissions for
   * the specified country.
   * A compatible vehicle is a vehicle that:
   * <ol>
   * <li>
   * has the hardware required for internet connectivity,
   * </li>
   * <li>
   * belongs to the makes and models Smartcar supports, and
   * </li>
   * <li>
   * supports the permissions.
   * </li>
   * </ol>
   * @param vin the VIN (Vehicle Identification Number) of the vehicle.
   * @param scope An array of permissions. The valid permissions are found in the API Reference.
   * @param country An optional country code according to [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
   *
   * @return false if the vehicle is not compatible in the specified country. true if the vehicle is likely compatible.
   *
   * @throws SmartcarException when the request is unsuccessful
   */
  public boolean isCompatible(String vin, String[] scope, String country) throws SmartcarException {
    String apiUrl = this.getApiUrl();
    HttpUrl url =
        HttpUrl.parse(apiUrl)
            .newBuilder()
            .addPathSegment("compatibility")
            .addQueryParameter("vin", vin)
            .addQueryParameter("scope", String.join(" ", scope))
            .addQueryParameter("country", country)
            .build();

    Request request =
        new Request.Builder()
            .url(url)
            .header("Authorization", this.basicAuthorization)
            .addHeader("User-Agent", AuthClient.USER_AGENT)
            .get()
            .build();

    return AuthClient.execute(request, Compatibility.class).getData().getCompatible();
  }
}
