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

    void setBaseUrl(String url){
      this.url = url;
    }

    private Request request(){
      return new Request.Builder()
        .url(this.url)
        .header("Authorization", this.auth)
        .post(this.body)
        .build();
    }

    String code(String code, String redirectUri)
    throws Exceptions.SmartcarException {
      this.body = new FormBody.Builder()
        .add("grant_type", "authorization_code")
        .add("code", code)
        .add("redirect_uri", redirectUri)
        .build();
      return Util.call(request());
    }

    String token(String refreshToken)
    throws Exceptions.SmartcarException {
      this.body = new FormBody.Builder()
        .add("grant_type", "refresh_token")
        .add("refresh_token", refreshToken)
        .build();
      return Util.call(request());
    }
  }