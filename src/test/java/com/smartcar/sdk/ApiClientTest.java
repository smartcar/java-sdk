package com.smartcar.sdk;

import com.smartcar.sdk.data.VehicleEngineOil;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Test Suite: ApiClient */
public class ApiClientTest {

  @Test
  public void testUserAgent() {
    // version has to be null because the package isn't built yet
    String regex = "^Smartcar/DEVELOPMENT \\((\\w+); (\\w+)\\) Java v(\\d+\\.\\d+\\.\\d_\\d+) .*";
    Assert.assertTrue(ApiClient.USER_AGENT.matches(regex));
  }

  @Test
  public void testKeyParsing() {
    String testData = "{\"lifeRemaining\":0.86}";

    VehicleEngineOil data = ApiClient.gson.create().fromJson(testData, VehicleEngineOil.class);
    Assert.assertTrue(data.getLifeRemaining() == 0.86);
  }
}
