package com.smartcar.sdk;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.Auth;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import com.smartcar.sdk.ApiClient;

import okhttp3.*;

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

  private final String clientId;
  private final String clientSecret;
  private final String redirectUri;
  private final boolean testMode;

  /**
   * Builds a new AuthClient.
   *
   */
  public static class Builder {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private boolean testMode;

    public Builder() {
      this.clientId = System.getenv("SMARTCAR_CLIENT_ID");
      this.clientSecret = System.getenv("SMARTCAR_CLIENT_SECRET");
      this.redirectUri = System.getenv("SMARTCAR_REDIRECT_URI");
      this.testMode = false;
    }

    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    public Builder clientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
      return this;
    }

    public Builder redirectUri(String redirectUri) {
      this.redirectUri = redirectUri;
      return this;
    }

    public Builder testMode(boolean testMode) {
      this.testMode = testMode;
      return this;
    }

    public AuthClient build() {
      return new AuthClient(this);
    }
  }

  private AuthClient(Builder builder) {
    this.clientId = builder.clientId;
    this.clientSecret = builder.clientSecret;
    this.redirectUri = builder.redirectUri;
    this.testMode = builder.testMode;

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
   * A builder for creating Authorization URLs. Access through {@link AuthClient#authUrlBuilder(String[])}.
   */
  public class AuthUrlBuilder {
    private HttpUrl.Builder urlBuilder;
    private List<String> flags = null;

    public AuthUrlBuilder(String[] scope) {
      String origin = System.getenv("SMARTCAR_CONNECT_ORIGIN");
      if (origin == null) {
        origin = "https://connect.smartcar.com";
      }
      urlBuilder =
          HttpUrl.parse(origin + "/oauth/authorize")
              .newBuilder()
              .addQueryParameter("response_type", "code")
              .addQueryParameter("client_id", AuthClient.this.clientId)
              .addQueryParameter("redirect_uri", AuthClient.this.redirectUri)
              .addQueryParameter("mode", AuthClient.this.testMode ? "test" : "live")
              .addQueryParameter("scope", Utils.join(scope, " "));
    }

    public AuthUrlBuilder state(String state) {
      if (state != "") {
        urlBuilder.addQueryParameter("state", state);
      }
      return this;
    }

    public AuthUrlBuilder approvalPrompt(boolean approvalPrompt) {
      urlBuilder.addQueryParameter("approval_prompt", approvalPrompt ? "force" : "auto");
      return this;
    }

    public AuthUrlBuilder makeBypass(String make) {
      urlBuilder.addQueryParameter("make", make);
      return this;
    }

    public AuthUrlBuilder singleSelect(boolean singleSelect) {
      urlBuilder.addQueryParameter("single_select", Boolean.toString(singleSelect));
      return this;
    }

    public AuthUrlBuilder singleSelectVin(String vin) {
      this.singleSelect(true);
      urlBuilder.addQueryParameter("single_select_vin", vin);
      return this;
    }

    public AuthUrlBuilder addFlag(String name, String value) {
      String flag = name + ":" + value;
      this.flags.add(flag);
      return this;
    }

    public AuthUrlBuilder addFlag(String name, boolean value) {
      String flag = name + ":" + value;
      this.flags.add(flag);
      return this;
    }

    public String build() {
      if (this.flags != null && !this.flags.isEmpty()) {
        String[] flagStrings = this.flags.toArray(new String[0]);
        urlBuilder.addQueryParameter("flags", Utils.join(flagStrings, " "));
      }
      return urlBuilder.build().toString();
    }
  }

  /**
   * Executes an Auth API request.
   *
   * @param requestBody the request body to be included
   * @param options SmartcarAuthOptions
   * @return the parsed response
   * @throws SmartcarException if the API request fails
   */
  private Auth getTokens(RequestBody requestBody, SmartcarAuthOptions options) throws SmartcarException {
    String basicAuthorization = Credentials.basic(
            this.clientId,
            this.clientSecret
    );
    String origin = System.getenv("SMARTCAR_AUTH_ORIGIN");
    if (origin == null) {
      origin = "https://auth.smartcar.com";
    }

    HttpUrl.Builder urlBuilder = HttpUrl.parse(origin + "/oauth/token").newBuilder();
    if (options.getFlags() != null) {
      urlBuilder.addQueryParameter("flags", options.getFlags());
    }

    Request request =
            new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .header("Authorization", basicAuthorization)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", AuthClient.USER_AGENT)
                    .post(requestBody)
                    .build();

    return AuthClient.execute(request, Auth.class);
  }

  /**
   * Exchanges an authorization code for an access token.
   *
   * @param code the authorization code
   * @return the requested access token
   * @throws SmartcarException when the request is unsuccessful
   */
  public Auth exchangeCode(String code) throws SmartcarException {
    SmartcarAuthOptions options = new SmartcarAuthOptions.Builder().build();
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
  public Auth exchangeCode(String code, SmartcarAuthOptions options) throws SmartcarException {
    RequestBody requestBody =
            new FormBody.Builder()
                    .add("grant_type", "authorization_code")
                    .add("code", code)
                    .add("redirect_uri", this.redirectUri)
                    .build();

    return this.getTokens(requestBody, options);
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

    return this.getTokens(requestBody, options);
  }

  public String getClientId() {
    return this.clientId;
  }

  public String getClientSecret() {
    return this.clientSecret;
  }

  public String getRedirectUri() {
    return this.redirectUri;
  }
}
