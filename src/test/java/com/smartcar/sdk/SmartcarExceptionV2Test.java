package com.smartcar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.json.Json;

import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test Suite: SmartcarExceptionV2
 */
@PrepareForTest({
    SmartcarExceptionV2.class,
    Gson.class,
    JsonObject.class,
    Response.class,
    ResponseBody.class,
})
@PowerMockIgnore("javax.net.ssl.*")

public class SmartcarExceptionV2Test extends PowerMockTestCase {
  /**
   * Test throwing an SmartcarExceptionV2 with a message.
   */
  @Test
  public void testNewExceptionWithDescription() throws Exception {
    String testMessage = "test message";

    try {
      throw new SmartcarExceptionV2(testMessage);
    }
    catch (SmartcarExceptionV2 ex) {
      Assert.assertEquals(ex.getDescription(), testMessage);
    }
  }

  /**
   * Test throwing an SmartcarExceptionV2 with a message.
   */
  @Test
  public void testNewExceptionWithRequestAndResponse() throws Exception {
    String expectedResolution = "expected resolution";
    String expectedDescription = "expected description";
    String expectedType = "ACCOUNT_STATE";
    String expectedCode = "INVALID_CREDENTIALS";
    String expectedRequestId = "011660dc-8322-4064-a972-53826c8dff9c";
    int expectedStatusCode = 200;

    String errorString = "{\"type\": \"" + expectedType + "\"," +
      "\"code\": \"" + expectedCode + "\"," +
      "\"description\": \"" + expectedDescription + "\"," +
      "\"docURL\": null," +
      "\"statusCode\": 200," +
            "\"resolution\": \"" + expectedResolution + "\"," +
            "\"detail\": null," +
      "\"requestId\": \"" + expectedRequestId + "\"}";

    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.header(eq("sc-request-id"), eq(""))).thenReturn(expectedRequestId);
    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(errorString);
    when(mockResponse.code()).thenReturn(expectedStatusCode);

    SmartcarExceptionV2 ex = SmartcarExceptionV2.Factory(mockResponse);

    Assert.assertEquals(ex.getRequestId(), expectedRequestId);
    Assert.assertEquals(ex.getCode(), expectedCode);
    Assert.assertEquals(ex.getType(), expectedType);
    Assert.assertEquals(ex.getDescription(), expectedDescription);
    Assert.assertEquals(ex.getResolution(), expectedResolution);
    Assert.assertNull(ex.getDocURL());
    Assert.assertNull(ex.getDetail());
    Assert.assertEquals(ex.getStatusCode(), expectedStatusCode);
  }

  /**
   * Test SmartcarExceptionV2.Factory with message
   */
  @Test
  public void testSmartcarExceptionV2FactoryWithMessage() throws Exception {
    String expectedMessage = "expected message";
    String response = Json.createObjectBuilder()
              .add("description", expectedMessage)
              .build()
              .toString();
    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(response);

    SmartcarExceptionV2 ex = SmartcarExceptionV2.Factory(mockResponse);

    Assert.assertEquals(ex.getDescription(), expectedMessage);
  }

  /**
   * Test SmartcarExceptionV2.Factory with invalid json body
   */
  @Test
  public void testSmartcarExceptionV2FactoryWithInvalidJsonBody() throws Exception {
    String response = "invalid json - string body";
    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(response);

    SmartcarExceptionV2 ex = SmartcarExceptionV2.Factory(mockResponse);

    Assert.assertEquals(ex.getDescription(), response);
  }
}
