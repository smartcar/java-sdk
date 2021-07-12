package com.smartcar.sdk.data;

import java.util.Date;

/** A container for the authentication tokens obtained from the Smartcar OAuth 2.0 service. */
public class Auth extends ApiData {
  private String accessToken;
  private String refreshToken;
  private Date expiration;
  private Date refreshExpiration;

  /**
   * Initializes a new instance with the specified token values.
   *
   * @param accessToken the access token
   * @param refreshToken the refresh token
   * @param expiration the access token expiration timestamp
   * @param refreshExpiration the refresh token expiration timestamp
   */
  public Auth(
      final String accessToken,
      final String refreshToken,
      final Date expiration,
      final Date refreshExpiration) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiration = expiration;
    this.refreshExpiration = refreshExpiration;
  }

  /**
   * Determines whether or not the current auth token has expired.
   *
   * @return whether or not the token has expired
   */
  public boolean isExpired() {
    return !this.expiration.after(new Date());
  }

  /**
   * Returns the currently stored access token.
   *
   * @return the access token
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Returns the currently stored refresh token.
   *
   * @return the refresh token
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Returns the expiration timestamp for the current access token.
   *
   * @return the access token expiration timestamp
   */
  public Date getExpiration() {
    return expiration;
  }

  /**
   * Returns the expiration timestamp for the current refresh token.
   *
   * @return the refresh token expiration timestamp
   */
  public Date getRefreshExpiration() {
    return refreshExpiration;
  }

  /** @return a stringified representation of Auth */
  @Override
  public String toString() {
    return this.getClass().getName()
        + "{"
        + "accessToken='"
        + accessToken
        + '\''
        + ", refreshToken='"
        + refreshToken
        + '\''
        + ", expiration="
        + expiration
        + ", refreshExpiration="
        + refreshExpiration
        + '}';
  }
}
