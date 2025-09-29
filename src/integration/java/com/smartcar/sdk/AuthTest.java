package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import com.smartcar.sdk.helpers.AuthHelpers;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class AuthTest {
  private AuthClient client;
  private String authorizeUrl;

  @BeforeSuite
  public void beforeSuite() throws Exception {
    this.client = AuthHelpers.getConfiguredAuthClientBuilder().build();
    this.authorizeUrl = client.authUrlBuilder(new String[] {"read_vehicle_info"}).build();
  }

  @Test
  public void testExchangeCode() throws SmartcarException, InterruptedException {
    String code = AuthHelpers.runAuthFlow(this.authorizeUrl);
    // Add delay to prevent rate limiting
    Thread.sleep(5000);
    Auth access = client.exchangeCode(code);

    assertNotNull(access.getAccessToken());
    assertNotNull(access.getRefreshToken());
    assertNotNull(access.getExpiration());
    assertNotNull(access.getRefreshExpiration());
  }

  @Test
  public void testExchangeRefreshToken() throws SmartcarException, InterruptedException {
    String code = AuthHelpers.runAuthFlow(this.authorizeUrl);
    // Add delay to prevent rate limiting
    Thread.sleep(5000);
    Auth oldAccess = client.exchangeCode(code);
    // Add delay between token exchange calls
    Thread.sleep(5000);
    Auth newAccess = client.exchangeRefreshToken(oldAccess.getRefreshToken());

    assertNotNull(newAccess.getAccessToken());
    assertNotNull(newAccess.getRefreshToken());
    assertNotNull(newAccess.getExpiration());
    assertNotNull(newAccess.getRefreshExpiration());
  }

  @Test
  public void testOAuthError() throws InterruptedException {
    // Add delay to prevent rate limiting
    Thread.sleep(5000);
    boolean thrown = false;
    try {
      client.exchangeCode("bad code here");
    } catch (SmartcarException e) {
      thrown = true;
      Assert.assertEquals(e.getMessage(), "invalid_request:null - Invalid code: bad code here.");
    }
    Assert.assertTrue(thrown);
  }
}
