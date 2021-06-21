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

/** Test Suite: SmartcarExceptionV2 */
@PrepareForTest({
  SmartcarException.class,
  Gson.class,
  JsonObject.class,
  Response.class,
  ResponseBody.class,
})
@PowerMockIgnore("javax.net.ssl.*")
public class SmartcarExceptionTest extends PowerMockTestCase {

  @Test
  public void testSmartcarExceptionV1Error() {
    //
  }
}
