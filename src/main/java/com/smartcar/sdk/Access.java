package com.smartcar.sdk;

public final class Access {

  private final String accessToken, refreshToken, tokenType;
  private final long createdAt;
  private final int expiresIn;

  /**
   * Default constructor.
   *
   * @param accessToken
   * @param refreshToken
   * @param tokenType
   * @param expiresIn
   */
  public Access(String accessToken, String refreshToken, 
                String tokenType, int expiresIn) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.tokenType = tokenType;
    this.expiresIn = expiresIn;
    this.createdAt = this.now();
  }

  private long now() {
    return System.currentTimeMillis();
  }

  public boolean expired() {
    return this.now() > this.createdAt + this.expiresIn * 1000;
  }

  public String getAccessToken() {
    return this.accessToken;
  }

  public String getRefreshToken() {
    return this.refreshToken;
  }

  public String getTokenType() {
    return this.tokenType;
  }
}