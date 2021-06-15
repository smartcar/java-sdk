package com.smartcar.sdk;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.testng.Assert.*;

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
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import javax.json.Json;
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

/** Test Suite: AuthClient */
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
}
