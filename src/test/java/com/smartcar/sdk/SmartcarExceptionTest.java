package com.smartcar.sdk;

import org.testng.Assert;
import org.testng.annotations.Test;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import javax.json.Json;
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
    }
    catch (SmartcarException ex) {
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
    int expectedStatusCode = 200;

    String response = Json.createObjectBuilder()
            .add("error", expectedError)
            .add("message", expectedMessage)
            .add("code", expectedCode)
            .build()
            .toString();

    Response mockResponse = mock(Response.class);
    ResponseBody mockResponseBody = mock(ResponseBody.class);

    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(response);
    when(mockResponse.code()).thenReturn(expectedStatusCode);

    SmartcarException ex = SmartcarException.Factory(mockResponse);

    Assert.assertEquals(ex.getCode(), expectedCode);
    Assert.assertEquals(ex.getMessage(), expectedMessage);
    Assert.assertEquals(ex.getError(), expectedError);
    Assert.assertEquals(ex.getStatusCode(), expectedStatusCode);
  }
}
