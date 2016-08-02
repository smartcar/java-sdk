package com.smartcar.sdk;

public final class Access {

  private final String accessToken, refreshToken;
  private final long createdAt;
  private final int expiresIn;

  public Access(String accessToken, String refreshToken, int expiresIn){
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.createdAt = this.now();
  }

  private long now(){
    return System.currentTimeMillis();
  }

  public boolean expired(){
    return this.now() > this.createdAt + this.expiresIn * 1000;
  }

  public String getAccessToken(){
    return this.accessToken;
  }

  public String getRefreshToken(){
    return this.refreshToken;
  }
}