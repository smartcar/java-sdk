package com.smartcar.sdk;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test Suite: ApiClient
 */
public class ApiClientTest {

  @Test
  public void testUserAgent() {
    // version has to be null because the package isn't built yet
    String regex = "^Smartcar/null \\((\\w+); (\\w+)\\) Java v(\\d+\\.\\d+\\.\\d_\\d+) .*";
    Assert.assertTrue(ApiClient.USER_AGENT.matches(regex));
  }
}
