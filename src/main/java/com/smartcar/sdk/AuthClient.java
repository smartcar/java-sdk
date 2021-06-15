package com.smartcar.sdk;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.Auth;

import java.lang.reflect.Type;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/** Smartcar OAuth 2.0 Authentication Client */
public class AuthClient extends ApiClient {
  /** Custom deserializer for Auth data from the OAuth endpoint. */
  private class AuthDeserializer implements JsonDeserializer<Auth> {
    /**
     * Deserializes the OAuth auth endpoint JSON into a new Auth object.
     *
     * @param json the Json data being deserialized
     * @param typeOfT the type of the Object to deserialize to
     * @param context the deserialization context
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
  public String origin;
  public static String urlAccessToken = AuthClient.URL_ACCESS_TOKEN;



  /**
   * Builds a new AuthClient.
   */
  public static class Builder {
    private String clientId;
    private String basicAuthorization;
    private String redirectUri;
    private String[] scope;
    private boolean testMode;
    public String origin = AuthClient.URL_AUTHORIZE;

    public Builder() {
      // defaults
    }

    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    public Builder basicAuthorization(String basicAuthorization) {
      this.basicAuthorization = basicAuthorization;
      return this;
    }

    public Builder redirectUri(String redirectUri) {
      this.redirectUri = redirectUri;
      return this;
    }

    public Builder scope(String[] scope) {
      this.scope = scope;
      return this;
    }

    public Builder testMode(boolean testMode) {
      this.testMode = testMode;
      return this;
    }

    public Builder origin(String origin) {
      this.origin = origin;
      return this;
    }

    public AuthClient build() {
      return new AuthClient(this);
    }
  }

  private AuthClient(Builder builder) {
    this.clientId = builder.clientId;
    this.basicAuthorization = builder.basicAuthorization;
    this.redirectUri = builder.redirectUri;
    this.scope = builder.scope;
    this.testMode = builder.testMode;
    this.origin = builder.origin;

    AuthClient.gson.registerTypeAdapter(Auth.class, new AuthDeserializer());
  }

  /**
   * Creates an AuthUrlBuilder
   *
   * @param scope the permission scope requested
   * @return returns an instance of AuthUrlBuilder
   */
  public AuthUrlBuilder authUrlBuilder(String[] scope) {
    return new AuthUrlBuilder(scope);
  }

  /**
   * Executes an Auth API request.
   *
   * @param requestBody the request body to be included
   * @param options SmartcarAuthOptions
   * @return the parsed response
   * @throws SmartcarException if the API request fails
   */
  private Auth call(RequestBody requestBody, SmartcarAuthOptions options) throws SmartcarException {
    Request request =
        new Request.Builder()
            .url(options.getOrigin())
            .header("Authorization", this.basicAuthorization)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("User-Agent", AuthClient.USER_AGENT)
            .post(requestBody)
            .build();

    return AuthClient.execute(request, Auth.class);
  }

  /**
   * Executes an Auth API request.
   *
   * @param requestBody the request body to be included
   * @return the parsed response
   * @throws SmartcarException if the API request fails
   */
  private Auth call(RequestBody requestBody) throws SmartcarException {
    SmartcarAuthOptions options = new SmartcarAuthOptions.Builder().build();
    return this.call(requestBody, options);
  }

  /**
   * A builder for creating Authorization URLs. Access through {@link AuthClient#authUrlBuilder()}.
   */
  public class AuthUrlBuilder {
    private HttpUrl.Builder urlBuilder;

    public AuthUrlBuilder(String[] scope) {
      urlBuilder =
          HttpUrl.parse(AuthClient.this.urlAuthorize)
              .newBuilder()
              .addQueryParameter("response_type", "code")
              .addQueryParameter("client_id", AuthClient.this.clientId)
              .addQueryParameter("redirect_uri", AuthClient.this.redirectUri)
              .addQueryParameter("mode", AuthClient.this.testMode ? "test" : "live")
              .addQueryParameter("scope", Utils.join(scope, " "));
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
   * Exchanges an authorization code for an access token.
   *
   * @param code the authorization code
   * @return the requested access token
   * @throws SmartcarException when the request is unsuccessful
   */
  public Auth exchangeCode(String code) throws SmartcarException {
    Builder options = new Builder();
    return this.exchangeCode(code, options);
  }

  /**
   * Exchanges an authorization code for an access token.
   *
   * @param code the authorization code
   * @param options the SmartcarAuthOptions
   * @return the requested access token
   * @throws SmartcarException when the request is unsuccessful
   */
  public Auth exchangeCode(String code, Builder options) throws SmartcarException {
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
   * @return the requested access token
   * @throws SmartcarException when the request is unsuccessful
   */
  public Auth exchangeRefreshToken(String refreshToken) throws SmartcarException {
    SmartcarAuthOptions options = new SmartcarAuthOptions.Builder().build();
    return this.exchangeRefreshToken(refreshToken, options);
  }

  /**
   * Exchanges a refresh token for a new access token.
   *
   * @param refreshToken the refresh token
   * @param options the SmartcarAuthOptions
   * @return the requested access token
   * @throws SmartcarException when the request is unsuccessful
   */
  public Auth exchangeRefreshToken(String refreshToken, SmartcarAuthOptions options) throws SmartcarException {
    RequestBody requestBody =
            new FormBody.Builder()
                    .add("grant_type", "refresh_token")
                    .add("refresh_token", refreshToken)
                    .build();

    return this.call(requestBody, options);
  }
}
