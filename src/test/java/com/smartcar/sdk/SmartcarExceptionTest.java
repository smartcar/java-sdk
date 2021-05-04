package com.smartcar.sdk;

import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.json.Json;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test Suite: SmartcarException
 */
@PrepareForTest({
  SmartcarException.class,
  Gson.class,
  JsonObject.class,
  Response.class,
  ResponseBody.class,
})
@PowerMockIgnore("javax.net.ssl.*")
public class SmartcarExceptionTest extends PowerMockTestCase {
  /**
   * Test throwing an SmartcarException with a message.
   */
  @Test
  public void testNewExceptionWithMessage() throws Exception {
    String testMessage = "test message";

    try {
      throw new SmartcarException(testMessage);
    } catch (SmartcarException ex) {
      Assert.assertEquals(ex.getMessage(), testMessage);
    }
  }

  /**
   * Test throwing an SmartcarException with a message.
   */
  @Test
  public void testNewExceptionWithRequestAndResponse() throws Exception {
    String expectedError = "expected_error";
    String expectedMessage = "expected message";
    String expectedCode = "VS_000";
    String expectedRequestId = "011660dc-8322-4064-a972-53826c8dff9c";
    int expectedStatusCode = 200;

    String response =
        Json.createObjectBuilder()
            .add("error", expectedError)
            .add("message", expectedMessage)
            .add("code", expectedCode)
            .build()
            .toString();

    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.header(eq("sc-request-id"), eq(""))).thenReturn(expectedRequestId);
    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(response);
    when(mockResponse.code()).thenReturn(expectedStatusCode);

    SmartcarException ex = SmartcarException.Factory(mockResponse);

    Assert.assertEquals(ex.getRequestId(), expectedRequestId);
    Assert.assertEquals(ex.getCode(), expectedCode);
    Assert.assertEquals(ex.getMessage(), expectedMessage);
    Assert.assertEquals(ex.getError(), expectedError);
    Assert.assertEquals(ex.getStatusCode(), expectedStatusCode);
  }

  /**
   * Test SmartcarException.Factory with error_description
   */
  @Test
  public void testSmartcarExceptionFactoryWithErrorDescription() throws Exception {
    String expectedMessage = "expected message";
    String response =
        Json.createObjectBuilder().add("error_description", expectedMessage).build().toString();
    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(response);

    SmartcarException ex = SmartcarException.Factory(mockResponse);

    Assert.assertEquals(ex.getMessage(), expectedMessage);
  }

  /**
   * Test SmartcarException.Factory with message
   */
  @Test
  public void testSmartcarExceptionFactoryWithMessage() throws Exception {
    String expectedMessage = "expected message";
    String response = Json.createObjectBuilder().add("message", expectedMessage).build().toString();
    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(response);

    SmartcarException ex = SmartcarException.Factory(mockResponse);

    Assert.assertEquals(ex.getMessage(), expectedMessage);
  }

  /**
   * Test SmartcarException.Factory with invalid json body
   */
  @Test
  public void testSmartcarExceptionFactoryWithInvalidJsonBody() throws Exception {
    String response = "invalid json - string body";
    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(response);

    SmartcarException ex = SmartcarException.Factory(mockResponse);

    Assert.assertEquals(ex.getMessage(), response);
  }
}
