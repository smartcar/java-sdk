package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import org.testng.annotations.Test;

/**
 * Test Suite: Vehicle
 */
public class VehicleTest {
  @Test
  public void testGetAuthUrl() {
    String clientId = "66f498e1-9447-4333-a0c2-1889909c1b44";
    String clientSecret = "aef2d2c8-5674-44de-b852-5b4aee143cd5";
    String redirectUri = "http://localhost:4000/auth";
    String state = "foobar";

    AuthClient authClient = new AuthClient(clientId, clientSecret, redirectUri, true);

//    Assert.assertEquals(authClient.getAuthUrl(state), "");

    String code = "21b0f3c0-6f29-431f-ad99-6180d56e2c4f";

    Auth auth = null;
    try {
      auth = authClient.exchangeCode(code);
    } catch (SmartcarException ex) {
      System.out.println("Code: " + ex.getStatus());
      System.out.println("Message: " + ex.getMessage());
      ex.printStackTrace();
    }

//    Assert.assertEquals(auth.getAccessToken(), "");

//    String accessToken = auth.getAccessToken();


  }
}
