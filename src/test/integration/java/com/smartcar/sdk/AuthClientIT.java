package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@PrepareForTest(AuthClient.class)
public class AuthClientIT extends IntegrationTest {
  private AuthClient subject;

  /**
   * Initializes the subject under test.
   */
  @BeforeMethod
  public void beforeMethod() throws Exception {
    this.subject = new AuthClient(
        this.config.getProperty("auth.client_id"),
        this.config.getProperty("auth.client_secret"),
        this.config.getProperty("auth.redirect_uri"),
//        this.config.getProperty("auth.scope"),
        Boolean.parseBoolean(this.config.getProperty("auth.development"))
    );

    Whitebox.setInternalState(this.subject, "urlAuthorize", this.config.getProperty("auth.url_authorize"));
    Whitebox.setInternalState(this.subject, "urlAccessToken", this.config.getProperty("auth.url_access_token"));
  }


  @Test
  public void testAuthClientWithValidConfig() throws IOException, SmartcarException {
    String authUrl = this.subject.getAuthUrl();
    String authCode;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Please authorize access to your vehicle: " + authUrl);
    System.out.println("Enter the authorization token in the redirect URL:");

    authCode = reader.readLine();

    // Exchange code for auth token.
    Auth authData = this.subject.exchangeCode(authCode);

    Assert.assertEquals(authData.getAccessToken(), "");
    Assert.assertEquals(authData.getExpiration(), "");
    Assert.assertEquals(authData.getRefreshToken(), "");
    Assert.assertEquals(authData.getRefreshExpiration(), "");
  }
}
