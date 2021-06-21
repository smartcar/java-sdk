package com.smartcar.sdk;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartcar.sdk.data.ApiData;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.smartcar.sdk.data.Meta;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Headers;
import org.apache.commons.text.CaseUtils;

/** Provides the core functionality for API client objects. */
abstract class ApiClient {
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  protected static final String USER_AGENT =
      String.format(
          "Smartcar/%s (%s; %s) Java v%s %s",
          Smartcar.getSdkVersion(),
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
   * Sends the specified request, returning the raw response body.
   *
   * @param request the desired request to transmit
   * @return the response body
   * @throws SmartcarException if the request is unsuccessful
   */
  protected static Response execute(Request request) throws SmartcarException {
    try {
      Response response = ApiClient.client.newCall(request).execute();

      if (!response.isSuccessful()) {
        throw SmartcarException.Factory(response.code(), response.headers(), response.body());
      } else {
        return response;
      }
    } catch (IOException ex) {
      throw new SmartcarException.Builder().type("SDK_ERROR").description(ex.getMessage()).build();
    }
  }

  /**
   * Sends the specified request, parsing the response into the specified type. Wraps the request
   * with the unitSystem and age meta data.
   *
   * @param <T> the data container for the parsed response JSON
   * @param request the desired request to transmit
   * @param dataType the type into which the response will be parsed
   * @return the wrapped response
   * @throws SmartcarException if the request is unsuccessful
   */
  protected static <T extends ApiData> T execute(
      Request request, Class<T> dataType) throws SmartcarException {
    Response response = ApiClient.execute(request);
    T data;
    Meta meta;
    String bodyString = null;

    try {
      bodyString = response.body().string();
      data = ApiClient.gson.create().fromJson(bodyString, dataType);
      Headers headers = response.headers();
      JsonObject headerJson = new JsonObject();
      for (String header: response.headers().names()) {
        headerJson.addProperty(header, headers.get(header));
      };
      String headerJsonString = headerJson.toString();
      meta = ApiClient.gson.create().fromJson(headerJsonString, Meta.class);
      data.setMeta(meta);
    } catch (Exception ex) {
      throw new SmartcarException.Builder()
              .statusCode(response.code())
              .description(bodyString)
              .requestId(response.headers().get("SC-Request-Id"))
              .type("SDK_ERROR")
              .build();
    }

    return data;
  }
}
