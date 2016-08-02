package com.smartcar.sdk;

/* Smartcar packages */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import okhttp3.Credentials;

/* Test packages */
import org.testng.Assert;
import org.testng.annotations.Test;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.HttpUrl;

public class TestSmartcar {

  String clientId = "client-id";
  String clientSecret = "client-secret";
  String redirectUri = "https://redirect.uri";
  String[] scope = {
    "read_vehicle_info",
    "open_sunroof"
  };
  
  Smartcar client = new Smartcar(clientId, clientSecret, 
                                 redirectUri, scope);
  @Test
  public void testJoinWithNonEmptyArray(){

    Assert.assertEquals(
      Smartcar.join(new String[] {"one", "two", "three"}),
      "one two three"
    );
  }

  @Test
  public void testJoinWithEmptyArray(){

    Assert.assertEquals(
      Smartcar.join(new String[]{}),
      ""
    );
  }

  @Test 
  public void testGetRequestUriWithoutArguments(){

    Assert.assertEquals(
      Smartcar.getRequestUri(),
      "https://api.smartcar.com/v1.0/vehicles"
    );
  }

  @Test
  public void testGetRequestUriWithArguments(){

    Assert.assertEquals(
      Smartcar.getRequestUri("vehicle-id", "api-endpoint"),
      "https://api.smartcar.com/v1.0/vehicles/vehicle-id/api-endpoint"
    );
  }

  @Test
  public void testGetAuthUrl() 
    throws UnsupportedEncodingException {

    String returnedUrl = client.getAuthUrl("https://oem.smartcar.com");
    Assert.assertEquals(
      returnedUrl,
      "https://oem.smartcar.com/oauth/authorize?response_type=code" 
        + "&client_id=client-id&redirect_uri=https://redirect.uri" 
        + "&scope=read_vehicle_info%20open_sunroof", "UTF-8"
    );
    HttpUrl parsedUrl = HttpUrl.parse(returnedUrl);
    Assert.assertNotNull(parsedUrl);
    Assert.assertEquals(
      parsedUrl.pathSegments().get(0),
      "oauth"
    );
    Assert.assertEquals(
      parsedUrl.pathSegments().get(1),
      "authorize"
    );
    Assert.assertEquals(
      parsedUrl.queryParameter("response_type"),
      "code"
    );
    Assert.assertEquals(
      parsedUrl.queryParameter("client_id"), 
      clientId
    );
    Assert.assertEquals(
      parsedUrl.queryParameter("redirect_uri"),
      redirectUri
    );
    Assert.assertEquals(
      parsedUrl.queryParameter("scope"),
      "read_vehicle_info open_sunroof"
    );

  }
/*
  @Test
  public void testExchangeCode()
    throws Exception {
    
    MockWebServer server = new MockWebServer();
    
    client.exchangeCode("oauth-code");
    
    RecordedREquest request = server.takeRequest();

    Assert.assertEquals(
      "/oauth/token", 
      request.getPath()
    );
    Assert.assertEquals(
      Credentials.basic(clientId, clientSecret),
      request.getHeader("Authorization")
    );
  }
  */
}