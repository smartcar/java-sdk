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
  public void beforeSuite()  {
    this.client = AuthHelpers.getConfiguredAuthClientBuilder().build();
    this.authorizeUrl = client.authUrlBuilder(new String[] {"read_vehicle_info"}).build();
  }

  @Test
  public void testExchangeCode() throws SmartcarException {
    String code = AuthHelpers.runAuthFlow(this.authorizeUrl);
    Auth access = client.exchangeCode(code);

    assertNotNull(access.getAccessToken());
    assertNotNull(access.getRefreshToken());
    assertNotNull(access.getExpiration());
    assertNotNull(access.getRefreshExpiration());
  }

  @Test
  public void testExchangeRefreshToken() throws SmartcarException {
    String code = AuthHelpers.runAuthFlow(this.authorizeUrl);
    Auth oldAccess = client.exchangeCode(code);
    Auth newAccess = client.exchangeRefreshToken(oldAccess.getRefreshToken());

    assertNotNull(newAccess.getAccessToken());
    assertNotNull(newAccess.getRefreshToken());
    assertNotNull(newAccess.getExpiration());
    assertNotNull(newAccess.getRefreshExpiration());
  }

  @Test
  public void testOAuthError() {
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
