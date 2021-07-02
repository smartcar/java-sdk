package com.smartcar.sdk;

import com.google.gson.*;
import com.smartcar.sdk.data.Auth;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

/** Test Suite: AuthClient */
@PrepareForTest({
  AuthClient.class,
  ApiClient.class,
  Gson.class,
  HttpUrl.class,
  JsonArray.class,
  JsonObject.class,
  Request.class,
  Response.class,
  ResponseBody.class,

})
@PowerMockIgnore("javax.net.ssl.*")
public class AuthClientTest extends PowerMockTestCase {
  // Sample Constructor Args
  private final String sampleClientId = "cl13nt1d-t35t-46dc-aa25-bdd042f54e7d";
  private final String sampleClientSecret = "24d55382-843f-4ce9-a7a7-cl13nts3cr3t";
  private final String sampleRedirectUri = "https://example.com/";
  private final String expectedRequestId = "67127d3a-a08a-41f0-8211-f96da36b2d6e";
  private final String sampleRedirectUriEncoded = "https%3A%2F%2Fexample.com%2F";
  private final String[] sampleScope = {"read_vehicle_info", "read_location", "read_odometer"};
  private final boolean sampleTestMode = true;

  // Sample AuthClient.getAuthUrl Args
  private final String sampleState = "s4mpl3st4t3";
  private final boolean sampleForcePrompt = true;

  // Sample AuthClient.exchangeCode Arg
  private final String sampleCode = "";

  // Sample AuthClient.exchangeRefreshToken Arg
  private final String sampleRefreshToken = "";

  // Fake Auth Data
  private String fakeAccessToken = "F4K3_4CC355_T0K3N";
  private String fakeRefreshToken = "F4K3_R3FR35H_T0K3N";
  private Date fakeExpiration = new Date();
  private Date fakeRefreshExpiration = new Date();

  // Subject Under Test
  private AuthClient subject;


  private JsonElement loadJsonResource(String resourceName) throws FileNotFoundException {
    String fileName = String.format("src/test/resources/%s.json", resourceName);
    return JsonParser.parseReader(new FileReader(fileName));
  }

  private void loadAndEnqueueResponse(String resourceName) throws FileNotFoundException {
    JsonElement success = loadJsonResource(resourceName);
    MockResponse mockResponse = new MockResponse()
            .setBody(success.toString())
            .addHeader("sc-request-id", this.expectedRequestId);
    TestExecutionListener.mockWebServer.enqueue(mockResponse);
  }

  @Test
  @PrepareForTest(System.class)
  public void testAuthClientBuilder() {
    PowerMockito.mockStatic(System.class);
    PowerMockito.when(System.getenv("SMARTCAR_CLIENT_ID")).thenReturn(this.sampleClientId);
    PowerMockito.when(System.getenv("SMARTCAR_CLIENT_SECRET")).thenReturn(this.sampleClientSecret);
    PowerMockito.when(System.getenv("SMARTCAR_REDIRECT_URI")).thenReturn(this.sampleRedirectUri);

    AuthClient client = new AuthClient.Builder().build();

    Assert.assertEquals(client.getClientId(), this.sampleClientId);
    Assert.assertEquals(client.getClientSecret(), this.sampleClientSecret);
    Assert.assertEquals(client.getRedirectUri(), this.sampleRedirectUri);
  }

  @Test
  @PrepareForTest(System.class)
  public void testExchangeCode() throws FileNotFoundException, SmartcarException, InterruptedException {
    loadAndEnqueueResponse("AuthGetTokens");

    PowerMockito.mockStatic(System.class);
    PowerMockito.when(System.getenv("SMARTCAR_AUTH_ORIGIN"))
            .thenReturn("http://localhost:" + TestExecutionListener.mockWebServer.getPort());

    AuthClient client = new AuthClient.Builder()
            .clientId(this.sampleClientId)
            .clientSecret(this.sampleClientSecret)
            .redirectUri(this.sampleRedirectUri)
            .build();

    Auth auth = client.exchangeCode(this.sampleCode);

    Assert.assertEquals(auth.getAccessToken(), "cf7ba7e9-8c5d-417d-a99f-c386cfc235cc");
    Assert.assertNotNull(auth.getExpiration().toString());
    Assert.assertEquals(auth.getRefreshToken(), "3e565aed-d4b2-4296-9b4c-aec35825a6aa");
    Assert.assertNotNull(auth.getRefreshExpiration().toString());

    RecordedRequest request = TestExecutionListener.mockWebServer.takeRequest();

    Assert.assertEquals(request.getRequestUrl().toString(),
            "http://localhost:" + TestExecutionListener.mockWebServer.getPort() + "/oauth/token");
    // can only verify the truncated body :(
    Assert.assertEquals(request.getBody().toString(), "[size=77 text=grant_type=authorization_code&code=&redirect_uri=https%3A%2F%2Fe…]");
  }

  @Test
  @PrepareForTest(System.class)
  public void testExchangeCodeOptions() throws FileNotFoundException, SmartcarException, InterruptedException {
    loadAndEnqueueResponse("AuthGetTokens");

    PowerMockito.mockStatic(System.class);
    PowerMockito.when(System.getenv("SMARTCAR_AUTH_ORIGIN"))
            .thenReturn("http://localhost:" + TestExecutionListener.mockWebServer.getPort());

    AuthClient client = new AuthClient.Builder()
            .clientId(this.sampleClientId)
            .clientSecret(this.sampleClientSecret)
            .redirectUri(this.sampleRedirectUri)
            .build();

    SmartcarAuthOptions options = new SmartcarAuthOptions.Builder().addFlag("foo", "bar").addFlag("test", true).build();

    Auth auth = client.exchangeCode(this.sampleCode, options);

    Assert.assertEquals(auth.getAccessToken(), "cf7ba7e9-8c5d-417d-a99f-c386cfc235cc");
    Assert.assertNotNull(auth.getExpiration().toString());
    Assert.assertEquals(auth.getRefreshToken(), "3e565aed-d4b2-4296-9b4c-aec35825a6aa");
    Assert.assertNotNull(auth.getRefreshExpiration().toString());

    RecordedRequest request = TestExecutionListener.mockWebServer.takeRequest();

    Assert.assertEquals(request.getRequestUrl().toString(),
            "http://localhost:" + TestExecutionListener.mockWebServer.getPort() + "/oauth/token?flags=foo%3Abar%20test%3Atrue");
    // can only verify the truncated body :(
    Assert.assertEquals(request.getBody().toString(), "[size=77 text=grant_type=authorization_code&code=&redirect_uri=https%3A%2F%2Fe…]");
  }

  @Test
  @PrepareForTest(System.class)
  public void testExchangeRefreshToken() throws FileNotFoundException, SmartcarException, InterruptedException {
    loadAndEnqueueResponse("AuthRefreshTokens");

    PowerMockito.mockStatic(System.class);
    PowerMockito.when(System.getenv("SMARTCAR_AUTH_ORIGIN")).thenReturn("http://localhost:" + TestExecutionListener.mockWebServer.getPort());

    AuthClient client = new AuthClient.Builder()
            .clientId(this.sampleClientId)
            .clientSecret(this.sampleClientSecret)
            .redirectUri(this.sampleRedirectUri)
            .build();

    Auth auth = client.exchangeRefreshToken(this.sampleRefreshToken);

    Assert.assertEquals(auth.getAccessToken(), "11016e76-610c-41c6-9688-1f5613889932");
    Assert.assertNotNull(auth.getExpiration().toString());
    Assert.assertEquals(auth.getRefreshToken(), "09337f8a-da3a-46c0-95e7-9c19180b06c0");
    Assert.assertNotNull(auth.getRefreshExpiration().toString());

    RecordedRequest request = TestExecutionListener.mockWebServer.takeRequest();

    Assert.assertEquals(request.getRequestUrl().toString(),
            "http://localhost:" + TestExecutionListener.mockWebServer.getPort() + "/oauth/token");
    Assert.assertEquals(request.getBody().toString(), "[text=grant_type=refresh_token&refresh_token=]");
  }

  @Test
  public void testAuthUrlBuilderDefault() {
    PowerMockito.mockStatic(System.class);
    PowerMockito.when(System.getenv("SMARTCAR_CLIENT_ID")).thenReturn(this.sampleClientId);
    PowerMockito.when(System.getenv("SMARTCAR_CLIENT_SECRET")).thenReturn(this.sampleClientSecret);
    PowerMockito.when(System.getenv("SMARTCAR_REDIRECT_URI")).thenReturn(this.sampleRedirectUri);

    AuthClient client = new AuthClient.Builder().build();
    String authUrl = client.authUrlBuilder(this.sampleScope).build();
    Assert.assertEquals(authUrl, "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id=cl13nt1d-t35t-46dc-aa25-bdd042f54e7d&redirect_uri=https%3A%2F%2Fexample.com%2F&mode=live&scope=read_vehicle_info%20read_location%20read_odometer");
  }

  @Test
  public void testAuthUrlBuilderWithOptions() {
    PowerMockito.mockStatic(System.class);
    PowerMockito.when(System.getenv("SMARTCAR_CLIENT_ID")).thenReturn(this.sampleClientId);
    PowerMockito.when(System.getenv("SMARTCAR_CLIENT_SECRET")).thenReturn(this.sampleClientSecret);
    PowerMockito.when(System.getenv("SMARTCAR_REDIRECT_URI")).thenReturn(this.sampleRedirectUri);
    AuthClient client = new AuthClient.Builder().build();

    String authUrl = client.authUrlBuilder(this.sampleScope)
            .state("sampleState")
            .approvalPrompt(true)
            .makeBypass("TESLA")
            .singleSelect(true)
            .singleSelectVin("sampleVin")
            .addFlag("foo", "bar")
            .addFlag("test", true)
            .build();
    Assert.assertEquals(authUrl, "https://connect.smartcar.com/oauth/authorize?response_type=code&client_id=cl13nt1d-t35t-46dc-aa25-bdd042f54e7d&redirect_uri=https%3A%2F%2Fexample.com%2F&mode=live&scope=read_vehicle_info%20read_location%20read_odometer&state=sampleState&approval_prompt=force&make=TESLA&single_select=true&single_select=true&single_select_vin=sampleVin&flags=foo%3Abar%20test%3Atrue");
  }
}
