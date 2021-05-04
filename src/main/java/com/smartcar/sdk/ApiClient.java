package com.smartcar.sdk;

import com.google.gson.GsonBuilder;
import com.smartcar.sdk.data.ApiData;
import com.smartcar.sdk.data.SmartcarResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.text.CaseUtils;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

/**
 * Provides the core functionality for API client objects.
 */
abstract class ApiClient {
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static final String SDK_VERSION = ApiClient.getSdkVersion();
  private static String API_VERSION = "1.0";
  protected static final String URL_API = "https://api.smartcar.com";
  protected static final String USER_AGENT =
      String.format(
          "Smartcar/%s (%s; %s) Java v%s %s",
          ApiClient.SDK_VERSION,
          System.getProperty("os.name"),
          System.getProperty("os.arch"),
          System.getProperty("java.version"),
          System.getProperty("java.vm.name"));

  private static OkHttpClient client =
      new OkHttpClient.Builder().readTimeout(310, TimeUnit.SECONDS).build();

  private static String toCamelCase(String fieldName) {
    if (fieldName.contains("_")) { // checks for snake case
      return CaseUtils.toCamelCase(fieldName, false, new char[] {'_'});
    }
    return fieldName;
  }

  static GsonBuilder gson =
      new GsonBuilder().setFieldNamingStrategy((field) -> toCamelCase(field.getName()));

  /**
   * Retrieves the SDK version, falling back to DEVELOPMENT if we're not running
   * from a jar.
   *
   * @return the SDK version
   */
  private static String getSdkVersion() {
    String version = ApiClient.class.getPackage().getImplementationVersion();

    if (version == null) {
      version = "DEVELOPMENT";
    }

    return version;
  }

  /**
   * Sets the API version
   *
   * @param version API version to set
   */
  public static void setApiVersion(String version) {
    ApiClient.API_VERSION = version;
  }

  /**
   * Gets the URL used for API requests
   *
   * @return
   */
  static String getApiUrl() {
    return ApiClient.URL_API + "/v" + ApiClient.API_VERSION;
  }

  /**
   * Sends the specified request, returning the raw response body.
   *
   * @param request the desired request to transmit
   *
   * @return the response body
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  protected static Response execute(Request request) throws SmartcarException {
    try {
      Response response = ApiClient.client.newCall(request).execute();

      if (!response.isSuccessful()) {
        String url = String.valueOf(request.url());
        if (url.contains("v2.0")) {
          throw SmartcarExceptionV2.Factory(response);
        }
        throw SmartcarException.Factory(response);
      } else {
        return response;
      }
    } catch (IOException ex) {
      throw new SmartcarException(ex.getMessage());
    }
  }

  /**
   * Sends the specified request, parsing the response into the specified type.
   * Wraps the request with the unitSystem and age meta data.
   *
   * @param <T> the data container for the parsed response JSON
   * @param request the desired request to transmit
   * @param dataType the type into which the response will be parsed
   *
   * @return the wrapped response
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  protected static <T extends ApiData> SmartcarResponse<T> execute(
      Request request, Class<T> dataType) throws SmartcarException {
    Response response = ApiClient.execute(request);
    String body = null;

    try {
      body = response.body().string();
    } catch (IOException ex) {
      throw new SmartcarException(ex.getMessage());
    }

    T data = ApiClient.gson.create().fromJson(body, dataType);

    String unitSystem = response.header("sc-unit-system");
    String ageHeader = response.header("sc-data-age");
    String requestId = response.header("sc-request-id");

    SmartcarResponse<T> smartcarResponse = new SmartcarResponse<T>(data);
    smartcarResponse.setUnitSystem(unitSystem);
    smartcarResponse.setRequestId(requestId);

    if (ageHeader != null) {
      Instant age = Instant.parse(ageHeader);
      smartcarResponse.setAge(Date.from(age));
    }

    return smartcarResponse;
  }
}
