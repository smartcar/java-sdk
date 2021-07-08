package com.smartcar.sdk;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.ApiData;
import com.smartcar.sdk.data.Meta;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Provides the core functionality for API client objects. */
abstract class ApiClient {
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  /**
   * Retrieves the SDK version, falling back to DEVELOPMENT if we're not running from a jar.
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

  protected static final String USER_AGENT =
      String.format(
          "Smartcar/%s (%s; %s) Java v%s %s",
          getSdkVersion(),
          System.getProperty("os.name"),
          System.getProperty("os.arch"),
          System.getProperty("java.version"),
          System.getProperty("java.vm.name"));

  private static final OkHttpClient client =
      new OkHttpClient.Builder().readTimeout(310, TimeUnit.SECONDS).build();


  static GsonBuilder gson =
      new GsonBuilder().setFieldNamingStrategy((field) -> Utils.toCamelCase(field.getName()));

  /**
   * Builds a request object with common headers, using provided request parameters
   * @param url url for the request, including the query parameters
   * @param method http method
   * @param body request body
   * @param headers additional headers to set for the request
   * @return
   */
  protected static Request buildRequest(HttpUrl url, String method, RequestBody body, Map<String, String> headers) {
    Request.Builder request = new Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", ApiClient.USER_AGENT)
                    .method(method, body);

    headers.forEach(request::addHeader);

    return request.build();
  }

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
    String bodyString = "";

    try {
      bodyString = response.body().string();
      data = ApiClient.gson.create().fromJson(bodyString, dataType);
      Headers headers = response.headers();
      JsonObject headerJson = new JsonObject();
      for (String header: response.headers().names()) {
        headerJson.addProperty(header.toLowerCase(), headers.get(header));
      }
      String headerJsonString = headerJson.toString();
      meta = ApiClient.gson.create().fromJson(headerJsonString, Meta.class);
      data.setMeta(meta);
    } catch (Exception ex) {
      if (bodyString.equals("")) {
        bodyString = "Empty response body";
      }
      throw new SmartcarException.Builder()
              .statusCode(response.code())
              .description(bodyString)
              .requestId(response.headers().get("sc-request-id"))
              .type("SDK_ERROR")
              .build();
    }

    return data;
  }
}
