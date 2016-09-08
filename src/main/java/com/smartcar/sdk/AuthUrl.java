package com.smartcar.sdk;

import okhttp3.HttpUrl;
import java.util.HashMap;

public class AuthUrl {

  private String oem, id, redirectUri, scope, state;
  private boolean force;
  private static HashMap<String, String> oems = new HashMap<String, String>();

  public AuthUrl(String oem, String id, String redirectUri, String scope){
    this.oem = oem;
    this.id = id;
    this.redirectUri = redirectUri;
    this.scope = scope;
    this.force = false;
    this.oems.put("acura", "https://acura.smartcar.com");
    this.oems.put("audi", "https://audi.smartcar.com");
    this.oems.put("fiat", "https://fiat.smartcar.com");
    this.oems.put("ford", "https://ford.smartcar.com");
    this.oems.put("landrover", "https://landrover.smartcar.com");
    this.oems.put("mercedes", "https://mercedes.smartcar.com");
    this.oems.put("tesla", "https://tesla.smartcar.com");
    this.oems.put("volkswagen", "https://volkswagen.smartcar.com");
    this.oems.put("volvo", "https://volvo.smartcar.com");
    this.oems.put("hyundai", "https://hyundai.smartcar.com");
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
    HttpUrl parsed = HttpUrl.parse(this.oems.get(this.oem));
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
