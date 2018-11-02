package com.smartcar.sdk.data;

import java.util.Date;

/**
 * A container for the authentication tokens obtained from the Smartcar OAuth
 * 2.0 service.
 */
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
  public Auth(final String accessToken, final String refreshToken, final Date expiration, final Date refreshExpiration) {
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
   * Stores a new access token.
   *
   * @param accessToken the access token to be stored
   */
  public void setAccessToken(final String accessToken) {
    this.accessToken = accessToken;
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
   * Stores a new refresh token.
   *
   * @param refreshToken the refresh token
   */
  public void setRefreshToken(final String refreshToken) {
    this.refreshToken = refreshToken;
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
   * Stores a new expiration timestamp for the current access token.
   *
   * @param expiration the access token expiration timestamp
   */
  public void setExpiration(final Date expiration) {
    this.expiration = expiration;
  }

  /**
   * Returns the expiration timestamp for the current refresh token.
   *
   * @return the refresh token expiration timestamp
   */
  public Date getRefreshExpiration() {
    return refreshExpiration;
  }

  /**
   * Stores a new expiration timestamp for the current refresh token.
   *
   * @param refreshExpiration the refresh token expiration timestamp
   */
  public void setRefreshExpiration(final Date refreshExpiration) {
    this.refreshExpiration = refreshExpiration;
  }

  /**
   * @return a stringified representation of Auth
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
            "accessToken='" + accessToken + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", expiration=" + expiration +
            ", refreshExpiration=" + refreshExpiration +
            '}';
  }
}
