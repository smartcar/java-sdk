package com.smartcar.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Provides the core functionality for API client objects.
 */
abstract class ApiClient {
  private static final String SDK_VERSION = ApiClient.class.getPackage().getImplementationVersion();
  protected static final String USER_AGENT = "smartcar-java-sdk: " + ApiClient.SDK_VERSION;

  private static OkHttpClient client = new OkHttpClient();
  private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  /**
   * Sends the specified request, returning the raw response body.
   *
   * @param request the desired request to transmit
   *
   * @return the response body
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  protected static String execute(Request request) throws SmartcarException {
    try {
      Response response = ApiClient.client.newCall(request).execute();

      if(!response.isSuccessful()) {
        response.close();
        throw new SmartcarException(SmartcarException.Status.forCode(response.code()));
      }
      else {
        String result = response.body().string();
        response.close();
        return result;
      }
    } catch (IOException ex) {
      throw new SmartcarException(ex.getMessage());
    }
  }

  /**
   * Sends the specified request, parsing the response into the specified type.
   *
   * @param request the desired request to transmit
   * @param type the type into which the response will be parsed
   *
   * @return the parsed response
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  protected static <T extends ApiData> T execute(Request request, Class<T> type) throws SmartcarException {
    String body = ApiClient.execute(request);
    return ApiClient.gson.fromJson(body, type);
  }
}
