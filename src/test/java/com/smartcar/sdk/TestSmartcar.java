package com.smartcar.sdk;

/* Smartcar packages */
import okhttp3.Credentials;
import java.util.ArrayList;
import com.google.gson.Gson;

/* Test packages */
import org.testng.Assert;
import org.testng.annotations.Test;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.HttpUrl;
import java.io.IOException;

public class TestSmartcar {

  String CLIENT_ID = "client-id";
  String CLIENT_SECRET = "client-secret";
  String REDIRECT_URI = "https://redirect.uri";
  String[] scope = {
    "read_vehicle_info",
    "open_sunroof"
  };

  String API_PATH = "/v1.0/vehicles";
  String AUTH_PATH = "/oauth/token";
  String AUTHORIZATION = Credentials.basic(CLIENT_ID, CLIENT_SECRET);
  String ACCESS_TOKEN = "access-token";
  String REFRESH_TOKEN = "refresh-token";
  String TOKEN_TYPE = "Bearer";
  int EXPIRES_IN = 7200;
  String CODE = "code";

  final Gson gson = new Gson();
  MockResponse ACCESS_RESPONSE = new MockResponse().setBody(gson.toJson(
    new Access(ACCESS_TOKEN, REFRESH_TOKEN, TOKEN_TYPE, EXPIRES_IN)
  ));
  
  Smartcar client = new Smartcar(CLIENT_ID, CLIENT_SECRET, 
                                 REDIRECT_URI, scope);

  @Test
  public void testGetAuthUrl() {

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
      CLIENT_ID
    );
    Assert.assertEquals(
      parsedUrl.queryParameter("redirect_uri"),
      REDIRECT_URI
    );
    Assert.assertEquals(
      parsedUrl.queryParameter("scope"),
      "read_vehicle_info open_sunroof"
    );
  }

  @Test
  public void testGetAuthUrlWithBadInput(){
    Assert.assertNull(client.getAuthUrl("http:/oem .smartcar .com"));
  }

  @Test
  public void testExchangeCode()
  throws Exceptions.SmartcarException, IOException, InterruptedException {

    /*
    accept:    
      POST https://auth.smartcar.com/oauth/token
      Authorization AUTHORIZATION
      grant_type=authorization_code
      code=CODE
      redirect_uri=REDIRECT_URI

    reply:
      {
        "access_token": ACCESS_TOKEN,
        "token_type": TOKEN_TYPE,
        "expires_in": EXPIRES_IN,
        "refresh_token": REFRESH_TOKEN
      }
    */
    MockWebServer server = new MockWebServer();
    server.enqueue(ACCESS_RESPONSE);
    server.start();
    client.setAccessUrl(server.url(AUTH_PATH).toString());
    Access access = client.exchangeCode(CODE);
    RecordedRequest request = server.takeRequest();

    /* begin assertions */
    Assert.assertEquals(request.getHeader("Authorization"), AUTHORIZATION);
    Assert.assertEquals(
      request.getBody().readUtf8(),
      String.format("grant_type=%s&code=%s&redirect_uri=%s",
        "authorization_code", CODE, "https%3A%2F%2Fredirect.uri")
    );
    Assert.assertEquals(access.getAccessToken(), ACCESS_TOKEN);
    Assert.assertEquals(access.getRefreshToken(), REFRESH_TOKEN);
    Assert.assertEquals(access.getTokenType(), TOKEN_TYPE);
    Assert.assertFalse(access.expired());

    server.shutdown();
  }
  
  @Test
  public void testExchangeToken() 
  throws Exceptions.SmartcarException, IOException, InterruptedException {

    /* 
    accept:
      POST https://auth.smartcar.com/oauth/token
      Authorization AUTHORIZATION
      grant_type=refresh_token
      refresh_token=REFRESH_TOKEN

    reply:
      {
        "access_token": ACCESS_TOKEN,
        "token_type": TOKEN_TYPE,
        "expires_in": EXPIRES_IN,
        "refresh_token": REFRESH_TOKEN
      }
    */
    MockWebServer server = new MockWebServer();
    server.enqueue(ACCESS_RESPONSE);
    server.start();
    client.setAccessUrl(server.url(AUTH_PATH).toString());
    Access access = client.exchangeToken(REFRESH_TOKEN);
    RecordedRequest request = server.takeRequest();

    /* begin assertions */
    Assert.assertEquals(request.getHeader("Authorization"), AUTHORIZATION);
    Assert.assertEquals(
      request.getBody().readUtf8(),
      String.format("grant_type=%s&refresh_token=%s",
        "refresh_token", REFRESH_TOKEN)
    );
    Assert.assertEquals(access.getAccessToken(), ACCESS_TOKEN);
    Assert.assertEquals(access.getRefreshToken(), REFRESH_TOKEN);
    Assert.assertEquals(access.getTokenType(), TOKEN_TYPE);
    Assert.assertFalse(access.expired());

    server.shutdown();
  }

  @Test
  public void testExpiredToken()
  throws Exceptions.SmartcarException, IOException, InterruptedException {
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse().setBody(gson.toJson(
    new Access(ACCESS_TOKEN, REFRESH_TOKEN, TOKEN_TYPE, -100)
    )));
    server.start();
    client.setAccessUrl(server.url(AUTH_PATH).toString());
    Access access = client.exchangeToken(REFRESH_TOKEN);
    RecordedRequest request = server.takeRequest();

    /* begin assertions */
    Assert.assertTrue(access.expired());
    Assert.assertEquals(request.getHeader("Authorization"), AUTHORIZATION);

    server.shutdown();
  }

  @Test
  public void testGetVehicles()
  throws Exceptions.SmartcarException, IOException, InterruptedException {

    /* 
    accept:
      GET https://api.smartcar.com/v1.0/vehicles/
      Authorization Bearer ACCESS_TOKEN

    reply:
      {
        "vehicles": [
          "36ab27d0-fd9d-4455-823a-ce30af709ffc",
          "3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1"
        ]
      }    
    */
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse().setBody(
      "{\"vehicles\":[\"36ab27d0-fd9d-4455-823a-ce30af709ffc\"," +
      "\"3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1\"]}"
    ));
    server.start();
    client.setVehicleUrl(server.url(API_PATH).toString());
    ArrayList<Vehicle> vehicles = client.getVehicles(ACCESS_TOKEN);
    RecordedRequest request = server.takeRequest();

    /* begin assertions */
    Assert.assertEquals(
      request.getHeader("Authorization"),
      TOKEN_TYPE + " " + ACCESS_TOKEN
    );

    Assert.assertEquals(
      request.getPath(),
      API_PATH
    );

    Assert.assertEquals(
      vehicles.get(0).getVid(),
      "36ab27d0-fd9d-4455-823a-ce30af709ffc"
    );

    Assert.assertEquals(
      vehicles.get(1).getVid(),
      "3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1"
    );

    server.shutdown();
  }

  @Test
  public void testGetVehiclesWithPaging()
  throws Exceptions.SmartcarException, IOException, InterruptedException {
    int LIMIT = 10;
    int OFFSET = 0;
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse().setBody(
      "{\"vehicles\":[\"36ab27d0-fd9d-4455-823a-ce30af709ffc\"," +
      "\"3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1\"]}"
    ));
    server.start();
    client.setVehicleUrl(server.url(API_PATH).toString());
    ArrayList<Vehicle> vehicles = client.getVehicles(
      ACCESS_TOKEN, LIMIT, OFFSET);
    RecordedRequest request = server.takeRequest();

    /* begin assertions */
    Assert.assertEquals(
      request.getHeader("Authorization"),
      TOKEN_TYPE + " " + ACCESS_TOKEN
    );

    Assert.assertEquals(
      request.getPath(),
      String.format(API_PATH + "?limit=%s&offset=%s",LIMIT,OFFSET)
    );
    Assert.assertEquals(
      vehicles.get(0).getVid(),
      "36ab27d0-fd9d-4455-823a-ce30af709ffc"
    );

    Assert.assertEquals(
      vehicles.get(1).getVid(),
      "3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1"
    );

    server.shutdown(); 
  }
}