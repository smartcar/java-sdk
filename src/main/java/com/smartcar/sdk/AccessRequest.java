package com.smartcar.sdk;

import okhttp3.Request;
import okhttp3.FormBody;
import okhttp3.Credentials;

class AccessRequest {

    private final String id, secret, auth;
    private String url;
    private FormBody body;

    AccessRequest(String id, String secret){
      this.id = id;
      this.secret = secret;
      this.url = "https://auth.smartcar.com/oauth/token";
      this.auth = Credentials.basic(id, secret);
    }

    void setUrl(String url){
      this.url = url;
    }

    private Request request(){
      return new Request.Builder()
        .url(this.url)
        .header("Authorization", this.auth)
        .post(this.body)
        .build();
    }

    /**
     * Exchange a code for an access object
     * @param  code
     *    
     * @param  redirectUri the redirect uri used to obtain the
     *                     authorization code
     * @return The response body from the API, which is a JSON string 
     *         containing an access object. It should look like this:
     *          {
     *            "access_token": "...",
     *            "token_type": "Bearer",
     *            "expires_in": 7200,
     *            "refresh_token": "...",
     *          }     
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
     * Exchange a refresh token for an access object
     * 
     * @param  refreshToken 
     * 
     * @return JSON string containing new access object
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