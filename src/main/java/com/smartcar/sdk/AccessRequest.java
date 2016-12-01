package com.smartcar.sdk;

import okhttp3.Request;
import okhttp3.FormBody;
import okhttp3.Credentials;

class AccessRequest {

  private final String clientId, clientSecret, auth;
  private String url;
  private FormBody body;

  /**
   * Default constructor.
   *
   * @param clientId Application's client ID, provided by Smartcar
   * @param clientSecret Application's client secret, provided by Smartcar
   */
  AccessRequest(String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.url = "https://auth.smartcar.com/oauth/token";
    this.auth = Credentials.basic(clientId, clientSecret);
  }

  void setBaseUrl(String url) {
      this.url = url;
    }

  /**
   * Build the request.
   *
   * @return The request object
   */
  private Request request() {
      return new Request.Builder()
        .url(this.url)
        .header("Authorization", this.auth)
        .post(this.body)
        .build();
  }

  /**
   * Exchange code for access token.
   *
   * @param code
   * @param redirectUri
   * @return
   * @throws Exceptions.SmartcarException
   */
  String code(String code, String redirectUri)
    throws Exceptions.SmartcarException {
      this.body = new FormBody.Builder()
        .add("grant_type", "authorization_code")
        .add("code", code)
        .add("redirect_uri", redirectUri)
        .build();
      return Util.call(request());
  }

  /**
   * Fetch new access token using the refresh token.
   *
   * @param refreshToken
   * @return
   * @throws Exceptions.SmartcarException
   */
  String token(String refreshToken)
    throws Exceptions.SmartcarException {
      this.body = new FormBody.Builder()
        .add("grant_type", "refresh_token")
        .add("refresh_token", refreshToken)
        .build();
      return Util.call(request());
  }
}