package com.smartcar.sdk;

import okhttp3.HttpUrl;
import java.util.HashMap;
import java.util.Properties;

public class AuthUrl {

  private String oem, clientId, redirectUri, scope, state;
  private boolean forceApproval;

  /**
   * Default constructor.
   *
   * @param oem the OEM name
   * @param clientId the application's client ID
   * @param redirectUri the application's redirect URL
   * @param scope a list of scopes
   */
  public AuthUrl(String oem, String clientId, String redirectUri, String scope) {

    this.oem = oem;
    this.clientId = clientId;
    this.redirectUri = redirectUri;
    this.scope = scope;
    this.forceApproval = false;
  }

  public AuthUrl state(String state) {
    this.state = state;
    return this;
  }

  public AuthUrl forceApproval(boolean forceApproval) {
    this.forceApproval = forceApproval;
    return this;
  }

  /**
   * Build the authentication request and return it as a string.
   *
   * @return
   */
  public String toString() {
    HttpUrl parsed = HttpUrl.parse(OEM.LIST.get(this.oem));
    if (parsed != null) {
      HttpUrl.Builder partial = parsed.newBuilder()
        .addPathSegments("oauth/authorize")
        .addQueryParameter("response_type", "code")
        .addQueryParameter("client_id", this.clientId)
        .addQueryParameter("redirect_uri", this.redirectUri)
        .addQueryParameter("scope", this.scope)
        .addQueryParameter("approval_prompt", this.forceApproval ? "force" : "auto");

      if (this.state != null)
        partial.addQueryParameter("state", this.state);

      return partial.build().toString();
    }
    return null;
  }
}
