package com.smartcar.sdk;

import com.google.gson.GsonBuilder;
import com.smartcar.sdk.data.ApiData;
import com.smartcar.sdk.data.SmartcarResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.joda.time.DateTime;
import org.apache.commons.text.CaseUtils;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

/**
 * Provides the core functionality for API client objects.
 */
abstract class ApiClient {
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static final String SDK_VERSION = ApiClient.getSdkVersion();
  private static final String API_VERSION = "v1.0";
  protected static final String URL_API = "https://api.smartcar.com/" + ApiClient.API_VERSION;
  protected static final String USER_AGENT = String.format(
      "Smartcar/%s (%s; %s) Java v%s %s",
      ApiClient.SDK_VERSION,
      System.getProperty("os.name"),
      System.getProperty("os.arch"),
      System.getProperty("java.version"),
      System.getProperty("java.vm.name")
  );

  private static OkHttpClient client = new OkHttpClient.Builder()
    .readTimeout(300, TimeUnit.SECONDS)
    .callTimeout(360, TimeUnit.SECONDS)
    .build();

  private static String toCamelCase(String fieldName) {
    if (fieldName.contains("_")) { // checks for snake case
      return CaseUtils.toCamelCase(fieldName, false, new char[]{'_'});
    }
    return fieldName;
  }

  static GsonBuilder gson = new GsonBuilder().setFieldNamingStrategy((field) -> toCamelCase(field.getName()));

  public static String urlApi = ApiClient.URL_API;

  /**
   * Retrieves the SDK version, falling back to DEVELOPMENT if we're not running
   * from a jar.
   *
   * @return the SDK version
   */
  private static String getSdkVersion() {
    String version = ApiClient.class.getPackage().getImplementationVersion();

    if(version == null) {
      version = "DEVELOPMENT";
    }

    return version;
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

      if(!response.isSuccessful()) {
        throw SmartcarException.Factory(response);
      }
      else {
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
  protected static <T extends ApiData> SmartcarResponse<T> execute(Request request, Class<T> dataType) throws SmartcarException {
    Response response = ApiClient.execute(request);
    String body = null;

    try {
      body = response.body().string();
    } catch (IOException ex) {
      throw new SmartcarException(ex.getMessage());
    }

    T data = ApiClient.gson.create().fromJson(body, dataType);

    String unitHeader = response.header("sc-unit-system");

    String ageHeader = response.header("sc-data-age");

    if(ageHeader != null) {
      DateTime date = DateTime.parse(ageHeader);

      return new SmartcarResponse<T>(data, unitHeader, date.toDate());
    } else {
      return new SmartcarResponse<T>(data, unitHeader, null);
    }
  }
}
