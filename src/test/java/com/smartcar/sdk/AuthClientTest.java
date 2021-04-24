package com.smartcar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.Auth;
import com.smartcar.sdk.data.Compatibility;
import com.smartcar.sdk.data.RequestPaging;
import com.smartcar.sdk.data.SmartcarResponse;
import com.smartcar.sdk.data.VehicleIds;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import javax.json.Json;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.testng.Assert.*;

/**
 * Test Suite: AuthClient
 */
@PrepareForTest({
    AuthClient.class,
    Gson.class,
    HttpUrl.class,
    JsonArray.class,
    JsonObject.class,
    Request.class,
    Response.class,
    ResponseBody.class
})
@PowerMockIgnore("javax.net.ssl.*")
public class AuthClientTest extends PowerMockTestCase {
  // Sample Constructor Args
  private final String sampleClientId = "cl13nt1d-t35t-46dc-aa25-bdd042f54e7d";
  private final String sampleClientSecret = "24d55382-843f-4ce9-a7a7-cl13nts3cr3t";
  private final String sampleRedirectUri = "https://example.com/";
  private final String sampleRedirectUriEncoded = "https%3A%2F%2Fexample.com%2F";
  private final String[] sampleScope = {"read_vehicle_info", "read_location", "read_odometer"};
  private final boolean sampleTestMode = true;

  // Sample AuthClient.getAuthUrl Args
  private final String sampleState = "s4mpl3st4t3";
  private final boolean sampleForcePrompt = true;
  private final AuthClient.AuthVehicleInfo sampleAuthVehicleInfo = new AuthClient.AuthVehicleInfo.Builder()
    .setMake("TESLA")
    .build();



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

  /**
   * Initializes a basic AuthClient subject under test with simple defaults.
   */
  @BeforeMethod
  private void beforeMethod() {
    this.subject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleTestMode
    );
  }

  /**
   * Tests that a valid user ID is returned.
   *
   * @throws Exception when an error occurs
   */
  @Test
  public void testGetUserId() throws Exception {
    // Setup
    String expectedUserId = "9c58a58f-579e-4fce-b2fc-53a518271b8c";

    // Mock: okhttp 
    mockStatic(HttpUrl.class);
    Request.Builder mockBuilder = mock(Request.Builder.class);
    Request mockRequest = mock(Request.class);

    whenNew(Request.Builder.class).withNoArguments().thenReturn(mockBuilder);
    when(mockBuilder.url(any(HttpUrl.class))).thenReturn(mockBuilder);
    when(mockBuilder.header(any(String.class), any(String.class))).thenReturn(mockBuilder);
    when(mockBuilder.addHeader(any(String.class), any(String.class))).thenReturn(mockBuilder);
    when(mockBuilder.build()).thenReturn(mockRequest);

    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    String response = Json.createObjectBuilder()
            .add("id", expectedUserId)
            .build()
            .toString();

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(response);

    // Mock: ApiClient.execute()
    spy(ApiClient.class);
    doReturn(mockResponse).when(ApiClient.class, "execute", mockRequest);

    // Execute
    String actualUserId = AuthClient.getUserId(this.fakeAccessToken);

    // Assertions
    assertEquals(actualUserId, expectedUserId);
  }

  /**
   * Tests that a SmartcarException is thrown when JSON parsing fails.
   *
   * @throws Exception when an error occurs
   */
  @Test
  public void testGetUserIdThrowsSmartcarExceptionWhenParsingFails() throws Exception {
    // Mock: okhttp
    mockStatic(HttpUrl.class);
    Request.Builder mockBuilder = mock(Request.Builder.class);
    Request mockRequest = mock(Request.class);

    whenNew(Request.Builder.class).withNoArguments().thenReturn(mockBuilder);
    when(mockBuilder.url(any(HttpUrl.class))).thenReturn(mockBuilder);
    when(mockBuilder.header(any(String.class), any(String.class))).thenReturn(mockBuilder);
    when(mockBuilder.addHeader(any(String.class), any(String.class))).thenReturn(mockBuilder);
    when(mockBuilder.build()).thenReturn(mockRequest);

    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    // Mock: ApiClient.execute()
     spy(ApiClient.class);
     doReturn(mockResponse).when(ApiClient.class, "execute", mockRequest);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenThrow(IOException.class);

    // Execute
    try {
      AuthClient.getUserId(this.fakeAccessToken);
    } catch(Exception e) {
      assertEquals(e.getClass(), SmartcarException.class);
    }
  }

  /**
   * Tests that a SmartcarException is thrown when the API response body is
   * null.
   *
   * @throws NullPointerException when an error occurs
   */
  @Test
  public void testGetUserIdThrowsSmartcarExceptionWhenResponseBodyIsEmpty() throws Exception {
    // Mock: okhttp
    mockStatic(HttpUrl.class);
    Request.Builder mockBuilder = mock(Request.Builder.class);
    Request mockRequest = mock(Request.class);

    whenNew(Request.Builder.class).withNoArguments().thenReturn(mockBuilder);
    when(mockBuilder.url(any(HttpUrl.class))).thenReturn(mockBuilder);
    when(mockBuilder.header(any(String.class), any(String.class))).thenReturn(mockBuilder);
    when(mockBuilder.addHeader(any(String.class), any(String.class))).thenReturn(mockBuilder);
    when(mockBuilder.build()).thenReturn(mockRequest);

    Response mockResponse = mock(Response.class);

    when(mockResponse.body()).thenThrow(NullPointerException.class);

    // Mock: ApiClient.execute()
    spy(ApiClient.class);
    doReturn(mockResponse).when(ApiClient.class, "execute", mockRequest);

    // Execute
    try {
      AuthClient.getUserId(this.fakeAccessToken);
    } catch(Exception e) {
      assertEquals(e.getClass(), NullPointerException.class);
    }
  }

  /**
   * Tests the expected vehicle IDs are returned.
   *
   * @throws Exception when an error occurs
   */
  @Test
  public void testGetVehicleIds() throws Exception {
    // Setup
    String[] expectedVehicleIds = new String[]{
        "9f3a9f2b-1ee2-41ea-a1ae-a06a527282ad",
        "219fac76-40c7-401d-92b5-d3a1f85bb89e",
        "c66f9e9d-08f1-4050-bec5-06e4ed677305",
        "33e10fcb-71f7-4661-94ad-6c87e1f5a2fe"
    };
    int expectedCount = expectedVehicleIds.length;
    int expectedOffset = 0;

    // Mock
    RequestPaging mockRequestPaging = mock(RequestPaging.class);

    // Mock: okhttp
    mockStatic(HttpUrl.class);
    HttpUrl.Builder mockUrlBuilder = mock(HttpUrl.Builder.class);
    HttpUrl mockUrl = mock(HttpUrl.class);

    when(HttpUrl.parse(any(String.class))).thenReturn(mockUrl);
    when(mockUrl.newBuilder()).thenReturn(mockUrlBuilder);
    when(mockUrlBuilder.addQueryParameter(any(String.class), any(String.class))).thenReturn(mockUrlBuilder);
    when(mockUrlBuilder.build()).thenReturn(mockUrl);

    Request.Builder mockRequestBuilder = mock(Request.Builder.class);
    Request mockRequest = mock(Request.class);

    whenNew(Request.Builder.class).withNoArguments().thenReturn(mockRequestBuilder);
    when(mockRequestBuilder.url(any(HttpUrl.class))).thenReturn(mockRequestBuilder);
    when(mockRequestBuilder.header(any(String.class), any(String.class))).thenReturn(mockRequestBuilder);
    when(mockRequestBuilder.addHeader(any(String.class), any(String.class))).thenReturn(mockRequestBuilder);
    when(mockRequestBuilder.build()).thenReturn(mockRequest);

    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn("");

    // Mock: ApiClient.execute()
    spy(ApiClient.class);
    doReturn(mockResponse).when(ApiClient.class, "execute", mockRequest);

    // Mock: gson
    Gson mockGson = mock(Gson.class);
    JsonObject mockJsonBody = mock(JsonObject.class);
    JsonElement mockJsonElementPaging = mock(JsonElement.class);
    JsonObject mockJsonPaging = mock(JsonObject.class);
    JsonElement mockJsonElementCount = mock(JsonElement.class);
    JsonElement mockJsonElementOffset = mock(JsonElement.class);
    JsonElement mockJsonElementVehicles = mock(JsonElement.class);
    JsonArray mockJsonArrayVehicles = mock(JsonArray.class);

    whenNew(Gson.class).withNoArguments().thenReturn(mockGson);
    when(mockGson.fromJson(any(String.class), any(Class.class))).thenReturn(mockJsonBody);

    when(mockJsonBody.get("paging")).thenReturn(mockJsonElementPaging);
    when(mockJsonElementPaging.getAsJsonObject()).thenReturn(mockJsonPaging);

    when(mockJsonPaging.get("count")).thenReturn(mockJsonElementCount);
    when(mockJsonElementCount.getAsInt()).thenReturn(expectedCount);

    when(mockJsonPaging.get("offset")).thenReturn(mockJsonElementOffset);
    when(mockJsonElementOffset.getAsInt()).thenReturn(expectedOffset);

    when(mockJsonBody.get("vehicles")).thenReturn(mockJsonElementVehicles);
    when(mockJsonElementVehicles.getAsJsonArray()).thenReturn(mockJsonArrayVehicles);
    when(mockJsonArrayVehicles.size()).thenReturn(expectedCount);

    JsonElement[] mockJsonElements = new JsonElement[expectedCount];

    for(int i = 0; i < expectedCount; i++) {
      mockJsonElements[i] = mock(JsonElement.class);

      when(mockJsonElements[i].getAsString()).thenReturn(expectedVehicleIds[i]);
      when(mockJsonArrayVehicles.get(i)).thenReturn(mockJsonElements[i]);
    }

    // Execute
    SmartcarResponse<VehicleIds> actual = AuthClient.getVehicleIds(this.fakeAccessToken, mockRequestPaging);

    // Assert
    assertEquals(actual.getData().getVehicleIds(), expectedVehicleIds);
    assertEquals(actual.getPaging().getCount(), expectedCount);
    assertEquals(actual.getPaging().getOffset(), expectedOffset);
    assertNull(actual.getAge());
    assertNull(actual.getUnitSystem());
  }

  /**
   * Tests that a SmartcarException is thrown when JSON parsing fails.
   *
   * @throws Exception when an error occurs
   */
  @Test
  public void testGetVehicleIdsThrowsSmartcarExceptionWhenParsingFails() throws Exception {
    // Mock
    RequestPaging mockRequestPaging = mock(RequestPaging.class);

    // Mock: okhttp
    mockStatic(HttpUrl.class);
    HttpUrl.Builder mockUrlBuilder = mock(HttpUrl.Builder.class);
    HttpUrl mockUrl = mock(HttpUrl.class);

    when(HttpUrl.parse(any(String.class))).thenReturn(mockUrl);
    when(mockUrl.newBuilder()).thenReturn(mockUrlBuilder);
    when(mockUrlBuilder.addQueryParameter(any(String.class), any(String.class))).thenReturn(mockUrlBuilder);
    when(mockUrlBuilder.build()).thenReturn(mockUrl);

    Request.Builder mockRequestBuilder = mock(Request.Builder.class);
    Request mockRequest = mock(Request.class);

    whenNew(Request.Builder.class).withNoArguments().thenReturn(mockRequestBuilder);
    when(mockRequestBuilder.url(any(HttpUrl.class))).thenReturn(mockRequestBuilder);
    when(mockRequestBuilder.header(any(String.class), any(String.class))).thenReturn(mockRequestBuilder);
    when(mockRequestBuilder.addHeader(any(String.class), any(String.class))).thenReturn(mockRequestBuilder);
    when(mockRequestBuilder.build()).thenReturn(mockRequest);

    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenThrow(IOException.class);

    // Mock: ApiClient.execute()
    spy(ApiClient.class);
    doReturn(mockResponse).when(ApiClient.class, "execute", mockRequest);

    // Execute
    try {
      AuthClient.getVehicleIds(this.fakeAccessToken, mockRequestPaging);
    } catch(Exception e) {
      assertEquals(e.getClass(), SmartcarException.class);
    }
  }

  /**
   * Tests that the custom Auth deserializer used by gson produces the expected
   * Auth instance with the correct values.
   *
   * @throws IllegalAccessException if there is a problem accessing the custom deserializer from this scope
   * @throws InvocationTargetException when an error occurs inside the custom deserializer
   * @throws InstantiationException if the custom deserializer cannot be instantiated
   * @throws ClassNotFoundException if the custom deserializer cannot be found
   */
  @Test
  public void testAuthCustomDeserializer() throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
    // Setup
    Class clazz = Whitebox.getInnerClassType(AuthClient.class, "AuthDeserializer");
    Constructor constructor = Whitebox.getConstructor(clazz, AuthClient.class);
    JsonDeserializer<Auth> authDeserializer = (JsonDeserializer<Auth>) constructor.newInstance(this.subject);

    // Mock: JsonElement
    JsonElement mockJson = mock(JsonElement.class);
    JsonObject mockJsonObject = mock(JsonObject.class);
    JsonObject mockJsonExpiresIn = mock(JsonObject.class);
    JsonObject mockJsonAccessToken = mock(JsonObject.class);
    JsonObject mockJsonRefreshToken = mock(JsonObject.class);

    when(mockJson.getAsJsonObject()).thenReturn(mockJsonObject);
    when(mockJsonObject.get("expires_in")).thenReturn(mockJsonExpiresIn);
    when(mockJsonExpiresIn.getAsInt()).thenReturn(10);
    when(mockJsonObject.get("access_token")).thenReturn(mockJsonAccessToken);
    when(mockJsonAccessToken.getAsString()).thenReturn(this.fakeAccessToken);
    when(mockJsonObject.get("refresh_token")).thenReturn(mockJsonRefreshToken);
    when(mockJsonRefreshToken.getAsString()).thenReturn(this.fakeRefreshToken);

    // Mock: Calendar
    mockStatic(Calendar.class);
    Calendar mockExpiration = mock(Calendar.class);

    when(Calendar.getInstance()).thenReturn(mockExpiration);
    when(mockExpiration.getTime()).thenReturn(this.fakeExpiration);

    // Mock: Unused Parameters
    Type mockTypeOfT = mock(Type.class);
    JsonDeserializationContext mockContext = mock(JsonDeserializationContext.class);

    // Execute Test
    Auth actualAuth = authDeserializer.deserialize(mockJson, mockTypeOfT, mockContext);

    // Assertions
    assertEquals(actualAuth.getAccessToken(), this.fakeAccessToken);
    assertEquals(actualAuth.getRefreshToken(), this.fakeRefreshToken);
    assertEquals(actualAuth.getExpiration(), this.fakeExpiration);
    assertEquals(actualAuth.getRefreshExpiration(), this.fakeExpiration);
  }

  /**
   * Tests the constructor providing all required and optional arguments.
   */
  @Test
  public void testAuthClientConstructorTestModeWithAllValidArgsProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleTestMode
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test";

    assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  @Test
  public void testAuthClientConstructorLiveModeWithAllValidArgsProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        false
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=live";

    assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  /**
   * Tests the constructor, providing all required arguments, and the optional
   * development flag.
   */
  @Test
  public void testAuthClientConstructorWithDevelopmentProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleTestMode
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&mode=test";

    assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  /**
   * Tests the constructor, providing all required arguments, and the optional
   * scope argument.
   */
  @Test
  public void testAuthClientConstructorWithScopeProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=live";

    assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  /**
   * Tests the constructor, providing all required arguments, and no optional
   * arguments.
   */
  @Test
  public void testAuthClientConstructorWithoutOptionalArgsProducesExpectedAuthUrl() {
    AuthClient testSubject = new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&mode=live";

    assertEquals(testSubject.getAuthUrl(), expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when
   * state, forcePrompt, and authVehicleInfo arguments are all specified.
   */
  @Test
  public void testGetAuthUrlWithStateAndForcePromptAndAuthVehicleInfoProducesExpectedAuthUrl() {
    String actualAuthUrl = this.subject.getAuthUrl(
        this.sampleState,
        this.sampleForcePrompt,
        this.sampleAuthVehicleInfo
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=force" +
        "&state=" + this.sampleState +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test" +
        "&make=" + this.sampleAuthVehicleInfo.getMake();

    assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when both
   * state and forcePrompt arguments are specified.
   */
  @Test
  public void testGetAuthUrlWithStateAndForcePromptProducesExpectedAuthUrl() {
    String actualAuthUrl = this.subject.getAuthUrl(
        this.sampleState,
        this.sampleForcePrompt
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=force" +
        "&state=" + this.sampleState +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test";

    assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when both
   * state and authVehicleInfo arguments are specified.
   */
  @Test
  public void testGetAuthUrlWithStateAndAndAuthVehicleInfoProducesExpectedAuthUrl() {
    String actualAuthUrl = this.subject.getAuthUrl(
        this.sampleState,
        this.sampleAuthVehicleInfo
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&state=" + this.sampleState +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test" +
        "&make=" + this.sampleAuthVehicleInfo.getMake();

    assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when both
   * state and authVehicleInfo arguments are specified.
   */
  @Test
  public void testGetAuthUrlWithForcePromptAndAuthVehicleInfoProducesExpectedAuthUrl() {
    String actualAuthUrl = this.subject.getAuthUrl(
        this.sampleForcePrompt,
        this.sampleAuthVehicleInfo
    );
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=force" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test" +
        "&make=" + this.sampleAuthVehicleInfo.getMake();

    assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when the
   * optional state argument is specified.
   */
  @Test
  public void testGetAuthUrlWithState() {
    String actualAuthUrl = this.subject.getAuthUrl(this.sampleState);
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&state=" + this.sampleState +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test";

    assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when the
   * optional forcePrompt argument is specified.
   */
  @Test
  public void testGetAuthUrlWithForcePrompt() {
    String actualAuthUrl = this.subject.getAuthUrl(this.sampleForcePrompt);
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=force" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test";

    assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when the
   * optional authVehicleInfo argument is specified.
   */
  @Test
  public void testGetAuthUrlWithAuthVehicleInfo() {
    String actualAuthUrl = this.subject.getAuthUrl(this.sampleAuthVehicleInfo);
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test" +
        "&make=" + this.sampleAuthVehicleInfo.getMake();
        
    assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when no
   * arguments are specified.
   */
  @Test
  public void testGetAuthUrlWithNoArgs() {
    String actualAuthUrl = this.subject.getAuthUrl();
    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + this.sampleClientId +
        "&redirect_uri=" + this.sampleRedirectUriEncoded +
        "&approval_prompt=auto" +
        "&scope=read_vehicle_info%20read_location%20read_odometer" +
        "&mode=test";

    assertEquals(actualAuthUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when
   * AuthUrlBuilder is used setting all params.
   */
  @Test
  public void testAuthUrlBuilder() {
    AuthClient client = new AuthClient(
            this.sampleClientId,
            this.sampleClientSecret,
            this.sampleRedirectUri,
            this.sampleScope,
            true
    );
    String vin = "1234567890ABCDEFG";
    String[] flags = {"country:DE", "flag:suboption"};
    String authUrl = client.authUrlBuilder()
            .setApprovalPrompt(true)
            .setState("state")
            .setSingleSelect(true)
            .setSingleSelectVin(vin)
            .setMakeBypass("TESLA")
            .setFlags(flags)
            .build();

    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
            "?response_type=code" +
            "&client_id=" + this.sampleClientId +
            "&redirect_uri=" + this.sampleRedirectUriEncoded +
            "&mode=test" +
            "&scope=read_vehicle_info%20read_location%20read_odometer" +
            "&approval_prompt=force" +
            "&state=state" +
            "&single_select=true" +
            "&single_select_vin=" + vin +
            "&make=TESLA" +
            "&flags=country%3ADE%20flag%3Asuboption";

    assertEquals(authUrl, expectedAuthUrl);
  }

  /**
   * Tests that Auth URL generation produces the expected URL string when
   * AuthUrlBuilder is used empty.
   */
  @Test
  public void testEmptyAuthUrlBuilder() {
    AuthClient client = new AuthClient(
            this.sampleClientId,
            this.sampleClientSecret,
            this.sampleRedirectUri,
            this.sampleScope,
            false
    );
    String authUrl = client.authUrlBuilder()
            .build();

    String expectedAuthUrl = "https://connect.smartcar.com/oauth/authorize" +
            "?response_type=code" +
            "&client_id=" + this.sampleClientId +
            "&redirect_uri=" + this.sampleRedirectUriEncoded +
            "&mode=live" +
            "&scope=read_vehicle_info%20read_location%20read_odometer";

    assertEquals(authUrl, expectedAuthUrl);
  }

  /**
   * Verifies that exchanging a code returns a valid Auth object containing
   * a token.
   *
   * @throws Exception on failure
   */
  @Test
  public void testExchangeCode() throws Exception {
    // Setup
    AuthClient testSubject = spy(new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleTestMode
    ));

    Auth expected = new Auth(
        this.fakeAccessToken,
        this.fakeRefreshToken,
        this.fakeExpiration,
        this.fakeRefreshExpiration
    );

    // Mocks
    spy(ApiClient.class);
    PowerMockito.doReturn(
          spy(new SmartcarResponse<Auth>(expected))
        )
        .when(ApiClient.class, "execute", any(), any());

    // Execute
    Auth actual = testSubject.exchangeCode(this.sampleCode);

    // Assertions
    assertEquals(actual, expected);
  }

  /**
   * Verifies that exchanging a refresh token returns a valid Auth object
   * containing a token.
   *
   * @throws Exception on failure
   */
  @Test
  public void testExchangeRefreshToken() throws Exception {
    // Setup
    AuthClient testSubject = spy(new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleTestMode
    ));

    Auth expected = new Auth(
        this.fakeAccessToken,
        this.fakeRefreshToken,
        this.fakeExpiration,
        this.fakeRefreshExpiration);

    // Mocks
    spy(ApiClient.class);
    spy(SmartcarResponse.class);
    PowerMockito.doReturn(
          spy(new SmartcarResponse<Auth>(expected))
        )
        .when(ApiClient.class, "execute", any(), any());

    // Execute
    Auth actual = testSubject.exchangeRefreshToken(this.sampleRefreshToken);

    // Assertions
    assertEquals(actual, expected);
  }

  /**
   * Verifies that compatibility is returned. 
   *
   * @throws Exception on failure
   */
  @Test
  public void testIsCompatible() throws Exception {
    // Setup
    AuthClient testSubject = spy(new AuthClient(
        this.sampleClientId,
        this.sampleClientSecret,
        this.sampleRedirectUri,
        this.sampleScope,
        this.sampleTestMode
    ));

    Compatibility expected = new Compatibility(true);
    
    // Execute with scope
    String[] scope = new String[]{"read_location", "read_odometer"}; 
    
    // Mocks
    spy(ApiClient.class);
    spy(SmartcarResponse.class);
    PowerMockito.doReturn(
          spy(new SmartcarResponse<Compatibility>(expected))
        )
        .when(ApiClient.class, "execute", argThat(new CompatibilityRequest()), any());

    boolean compatibilityWScopes = testSubject.isCompatible("vin", scope);

    assertEquals(compatibilityWScopes, expected.getCompatible());
  }


  /**
   * Tests setting the api version to 2.0 and getting the api url that is used for subsequent requests
   */
  @Test
  public void testSetApiVersion() {
    AuthClient testSubject = new AuthClient(
            this.sampleClientId,
            this.sampleClientSecret,
            this.sampleRedirectUri,
            this.sampleTestMode
    );
    testSubject.setApiVersion("2.0");
    String url = AuthClient.getApiUrl();
    Assert.assertEquals(url, "https://api.smartcar.com/v2.0");
    testSubject.setApiVersion("1.0");
  }
}
