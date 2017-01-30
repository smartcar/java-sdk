package com.smartcar.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;

import com.google.gson.Gson;

import okhttp3.Credentials;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.io.IOException;

public class TestSmartcar {

  String ID = "client-id";
  String SECRET = "client-secret";
  String REDIRECT_URI = "https://redirect.uri";
  String[] scope = { "read_vehicle_info", "open_sunroof" };

  String API_PATH = "/v1.0/vehicles";
  String AUTH_PATH = "/oauth/token";
  String BASIC_AUTH = Credentials.basic(ID, SECRET);
  String ACCESS_TOKEN = "access-token";
  String REFRESH_TOKEN = "refresh-token";
  String TOKEN_TYPE = "Bearer";
  String BEARER_AUTH = TOKEN_TYPE + " " + ACCESS_TOKEN;
  String USER_AGENT = "";
  int EXPIRES_IN = 7200;
  String CODE = "code";

  private final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  MockResponse ACCESS_RESPONSE = new MockResponse().setBody(gson.toJson(
    new Access(ACCESS_TOKEN, REFRESH_TOKEN, TOKEN_TYPE, EXPIRES_IN)
  ));


  Smartcar client = new Smartcar(ID, SECRET, REDIRECT_URI, scope);
  MockWebServer server;
  RecordedRequest request;

  private void setup(MockResponse response, String path) {
    server = new MockWebServer();
    server.enqueue(response);
    try {
      server.start();
    } catch (IOException e) {
      System.out.println(e);
    }
    String version = ReadVersion.getVersionNumber();
    USER_AGENT = "smartcar-java-sdk:" + version;
    client.setBaseAccessUrl(server.url(path).toString());
    client.setBaseVehicleUrl(server.url(path).toString());
  }

  private void verify(String authentication) {
    try {
      request = server.takeRequest();
    } catch (InterruptedException e) {
      System.out.println(e);
    }
    Assert.assertEquals(request.getHeader("Authorization"), authentication);
    Assert.assertEquals(request.getHeader("User-Agent"), USER_AGENT);
  }

  @AfterMethod
  private void shutdown() throws IOException {
    server.shutdown();
  }

  @Test
  public void testGetAuthUrl() {
    String base = "https://ford.smartcar.com/oauth/authorize?response_type=code"
        + "&client_id=client-id&redirect_uri=https://redirect.uri"
        + "&scope=read_vehicle_info%20open_sunroof";
    AuthUrl auth = client.getAuthUrl("ford");
    Assert.assertEquals(
      auth.toString(),
      base + "&approval_prompt=auto"
    );
    auth.state("XYZ").forceApproval(true);
    Assert.assertEquals(
      auth.toString(),
      base + "&approval_prompt=force&state=XYZ"
    );
  }

  @Test
  public void testGetAuthUrlNoScope() {
    String base = "https://ford.smartcar.com/oauth/authorize?response_type=code"
            + "&client_id=client-id&redirect_uri=https://redirect.uri";

    Smartcar noScopeClient = new Smartcar(ID, SECRET, REDIRECT_URI);

    AuthUrl auth = noScopeClient.getAuthUrl("ford");
    Assert.assertEquals(
            auth.toString(),
            base + "&approval_prompt=auto"
    );
    auth.state("XYZ").forceApproval(true);
    Assert.assertEquals(
            auth.toString(),
            base + "&approval_prompt=force&state=XYZ"
    );
  }

  @Test
  public void testExchangeCode() throws Exceptions.SmartcarException {

    setup(ACCESS_RESPONSE, AUTH_PATH);
    Access access = client.exchangeCode(CODE);
    verify(BASIC_AUTH);

    /* begin assertions */
    Assert.assertEquals(
      request.getBody().readUtf8(),
      String.format("grant_type=%s&code=%s&redirect_uri=%s",
        "authorization_code", CODE, "https%3A%2F%2Fredirect.uri")
    );
    Assert.assertEquals(access.getAccessToken(), ACCESS_TOKEN);
    Assert.assertEquals(access.getRefreshToken(), REFRESH_TOKEN);
    Assert.assertEquals(access.getTokenType(), TOKEN_TYPE);
    Assert.assertFalse(access.expired());
  }

  @Test
  public void testExchangeToken()
  throws Exceptions.SmartcarException {

    setup(ACCESS_RESPONSE, AUTH_PATH);
    Access access = client.exchangeToken(REFRESH_TOKEN);
    verify(BASIC_AUTH);

    /* begin assertions */
    Assert.assertEquals(
      request.getBody().readUtf8(),
      "grant_type=refresh_token&refresh_token=" + REFRESH_TOKEN
    );
    Assert.assertEquals(access.getAccessToken(), ACCESS_TOKEN);
    Assert.assertEquals(access.getRefreshToken(), REFRESH_TOKEN);
    Assert.assertEquals(access.getTokenType(), TOKEN_TYPE);
    Assert.assertFalse(access.expired());
  }

  @Test
  public void testExpiredToken() throws Exceptions.SmartcarException {

    setup(
      new MockResponse().setBody(gson.toJson(
        new Access(ACCESS_TOKEN, REFRESH_TOKEN, TOKEN_TYPE, -100)
      )
    ), AUTH_PATH);

    Access access = client.exchangeToken(REFRESH_TOKEN);
    verify(BASIC_AUTH);

    /* begin assertions */
    Assert.assertTrue(access.expired());
  }

  @Test
  public void testGetVehicles() throws Exceptions.SmartcarException {

    setup(new MockResponse().setBody(
      "{\"vehicles\":[\"36ab27d0-fd9d-4455-823a-ce30af709ffc\"," +
      "\"3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1\"]}"
    ), API_PATH);

    Api.Vehicles vehicles = client.getVehicles(ACCESS_TOKEN);
    verify(BEARER_AUTH);

    /* begin assertions */
    Assert.assertEquals(request.getPath(), API_PATH);

    Assert.assertEquals(
      vehicles.vehicles[0],
      "36ab27d0-fd9d-4455-823a-ce30af709ffc"
    );

    Assert.assertEquals(
      vehicles.vehicles[1],
      "3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1"
    );
  }

  @Test
  public void testGetVehiclesWithPaging() throws Exceptions.SmartcarException {
    int LIMIT = 10;
    int OFFSET = 0;

    setup(new MockResponse().setBody(
      "{\"vehicles\":[\"36ab27d0-fd9d-4455-823a-ce30af709ffc\"," +
      "\"3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1\"]}"
    ), API_PATH);

    Api.Vehicles vehicles = client.getVehicles(ACCESS_TOKEN, LIMIT, OFFSET);
    verify(BEARER_AUTH);

    /* begin assertions */
    Assert.assertEquals(
      request.getPath(),
      String.format(API_PATH + "?limit=%s&offset=%s", LIMIT, OFFSET)
    );
    Assert.assertEquals(
      vehicles.vehicles[0],
      "36ab27d0-fd9d-4455-823a-ce30af709ffc"
    );

    Assert.assertEquals(
      vehicles.vehicles[1],
      "3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1"
    );
  }
}
