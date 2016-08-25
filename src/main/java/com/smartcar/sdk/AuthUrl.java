package com.smartcar.sdk;

import okhttp3.HttpUrl;

public class AuthUrl {

  private String oem, id, redirectUri, scope, state;
  private boolean force;

  public AuthUrl(String oem, String id, String redirectUri, String scope){
    this.oem = oem;
    this.id = id;
    this.redirectUri = redirectUri;
    this.scope = scope;
    this.force = false;
  }

  public AuthUrl state(String state){
    this.state = state;
    return this;
  }

  public AuthUrl forceApproval(boolean force){
    this.force = force;
    return this;
  }

  public String toString(){
    HttpUrl parsed = HttpUrl.parse("https://" + this.oem + ".smartcar.com");
    if (parsed != null){
      HttpUrl.Builder partial = parsed.newBuilder()
        .addPathSegments("oauth/authorize")
        .addQueryParameter("response_type", "code")
        .addQueryParameter("client_id", this.id)
        .addQueryParameter("redirect_uri", this.redirectUri)
        .addQueryParameter("scope", this.scope)
        .addQueryParameter("approval_prompt", this.force ? "force" : "auto");

      if (this.state != null)
        partial.addQueryParameter("state", this.state);

      return partial.build().toString();
    }
    return null;
  }
}