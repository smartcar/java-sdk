package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Test Suite: Auth
 */
public class AuthTest {
  private final String sampleAccessToken = "D3F4ULTACC355T0K3N";
  private final String sampleRefreshToken = "D3F4ULTR3FR35HT0K3N";
  private final Date sampleExpiration = new Date(1321038671011L);        // 2011/11/11 11:11:11 AM UTC
  private final Date sampleRefreshExpiration = new Date(1321038671011L); // 2011/11/11 11:11:11 AM UTC

  private Auth subject;

  /**
   * Initializes a basic Auth subject under test with simple defaults.
   */
  @BeforeMethod
  public void before() {
    this.subject = new Auth(this.sampleAccessToken, this.sampleRefreshToken, this.sampleExpiration, this.sampleRefreshExpiration);
  }

  /**
   * Test Setter/Getter: accessToken
   */
  @Test
  public void testGetSetAccessToken() {
    Assert.assertEquals(subject.getAccessToken(), this.sampleAccessToken);
    subject.setAccessToken("ACC355T0K3N");
    Assert.assertEquals(subject.getAccessToken(), "ACC355T0K3N");
  }

  /**
   * Test Setter/Getter: refreshToken
   */
  @Test
  public void testGetSetRefreshToken() {
    Assert.assertEquals(subject.getRefreshToken(), this.sampleRefreshToken);
    subject.setRefreshToken("R3FR35HT0K3N");
    Assert.assertEquals(subject.getRefreshToken(), "R3FR35HT0K3N");
  }

  /**
   * Test Setter/Getter: expiration
   */
  @Test
  public void testGetSetExpiration() {
    Assert.assertEquals(subject.getExpiration(), this.sampleExpiration);
    Date anotherDate = new Date(1355343132012L); // 2012/12/12 12:12:12 AM UTC
    subject.setExpiration(anotherDate);
    Assert.assertEquals(subject.getExpiration(), anotherDate);
  }

  /**
   * Test Setter/Getter: refreshExpiration
   */
  @Test
  public void testGetSetRefreshExpiration() {
    Assert.assertEquals(subject.getRefreshExpiration(), this.sampleRefreshExpiration);
    Date anotherDate = new Date(1355343132012L); // 2012/12/12 12:12:12 AM UTC
    subject.setRefreshExpiration(anotherDate);
    Assert.assertEquals(subject.getRefreshExpiration(), anotherDate);
  }
}
