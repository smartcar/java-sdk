package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import okhttp3.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Smartcar OAuth 2.0 Authentication Client */
public class AuthClient {

  private final String clientId;
  private final String clientSecret;
  private final String redirectUri;
  private final String mode;

  /**
   * Builds a new AuthClient.
   *
   */
  public static class Builder {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String mode;
    private final Set<String> validModes = Stream.of("test", "live", "simulated").collect(Collectors.toSet());

    public Builder() {
      this.clientId = System.getenv("SMARTCAR_CLIENT_ID");
      this.clientSecret = System.getenv("SMARTCAR_CLIENT_SECRET");
      this.redirectUri = System.getenv("SMARTCAR_REDIRECT_URI");
      this.mode = "live";
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

    /**
     * @deprecated use {@link Builder#mode(String)} instead.
     */
    @Deprecated
    public Builder testMode(boolean testMode) {
      this.mode = testMode ? "test" : "live";
      return this;
    }

    public Builder mode(String mode) throws Exception {
      if (!this.validModes.contains(mode)) {
        throw new Exception(
          "The \"mode\" parameter MUST be one of the following: \"test\", \"live\", \"simulated\""
        );
      }

      this.mode = mode;
      return this;
    }


    public AuthClient build() throws Exception {
      if (this.clientId == null) {
        throw new Exception("clientId must be defined");
      }
      if (this.clientSecret == null) {
        throw new Exception("clientSecret must be defined");
      }
      if (this.redirectUri == null) {
        throw new Exception("redirectUri must be defined");
      }
      return new AuthClient(this);
    }
  }

  private AuthClient(Builder builder) {
    this.clientId = builder.clientId;
    this.clientSecret = builder.clientSecret;
    this.redirectUri = builder.redirectUri;
    this.mode = builder.mode;
  }

  /**
   * Creates an AuthUrlBuilder
   *
   * @param scope the permission scope(s) requested
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
    private String mode = AuthClient.this.mode;
    private List<String> flags = new ArrayList<>();

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
              .addQueryParameter("mode", AuthClient.this.mode)
              .addQueryParameter("scope", Utils.join(scope, " "));
    }

    public AuthUrlBuilder state(String state) {
      if (!state.equals("")) {
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


      public AuthUrlBuilder addUser(String user) {
        urlBuilder.addQueryParameter("user", user);
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

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", basicAuthorization);
    headers.put("Content-Type", "application/x-www-form-urlencoded");
    Request request = ApiClient.buildRequest(urlBuilder.build(), "POST", requestBody, headers);

    return ApiClient.execute(request, Auth.class);
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
