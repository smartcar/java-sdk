package com.smartcar.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.smartcar.sdk.data.ApiData;
import com.smartcar.sdk.data.SmartcarResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Provides the core functionality for API client objects.
 */
abstract class ApiClient {
  private static final String SDK_VERSION = ApiClient.class.getPackage().getImplementationVersion();
  protected static final String USER_AGENT = "smartcar-java-sdk: " + ApiClient.SDK_VERSION;

  private static OkHttpClient client = new OkHttpClient();
  static GsonBuilder gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  /**
   * Sends the specified request, returning the raw response body.
   *
   * @param request the desired request to transmit
   *
   * @return the response body
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  private static Response execute(Request request) throws SmartcarException {
    try {
      Response response = ApiClient.client.newCall(request).execute();

      if(!response.isSuccessful()) {
        response.close();
        throw new SmartcarException(SmartcarException.Status.forCode(response.code()));
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
    } catch (IOException e) {
      throw new SmartcarException(e.getMessage());
    }

    T data = ApiClient.gson.create().fromJson(body, dataType);

    String unitHeader = response.header("sc-unit-system");

    String ageHeader = response.header("sc-data-age");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    try {
      return new SmartcarResponse<T>(data, unitHeader, df.parse(ageHeader));
    } catch (ParseException e) {
      throw new SmartcarException(e.getMessage());
    }
  }
}
