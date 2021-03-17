package com.smartcar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.Response;

import javax.json.Json;
import java.io.IOException;

/**
 * Thrown when the Smartcar API V2 library encounters a problem.
 */
public class SmartcarExceptionV2 extends Exception {

  static Gson gson = new Gson();

  // standard error properties
  private int statusCode;
  private String code;
  private String requestId;
  private String type;
  private String description;
  private String resolution;
  private String detail;
  private String docURL;

  // oauth error properties
  private String error;
  private String errorDescription;
  private String errorURI;

  public SmartcarExceptionV2(String type, String code, String description, String resolution, String detail, String docURL, int statusCode, String requestId) {
    super(description);
    this.type = type;
    this.code = code;
    this.description = description;
    this.resolution = resolution;
    this.detail = detail;
    this.docURL = docURL;
    this.statusCode = statusCode;
    this.requestId = requestId;
  }

  /**
   * Initializes a new Smartcar API v2 exception with the specified message.
   *
   * @param message a message associated with the exception
   */
  public SmartcarExceptionV2(final String message) {
    this.description = message;
  }
  
  /**
   * Returns the vehicle state error code associated with the SmartcarExceptionV2.
   *
   * @return the vehicle state error code
   */
  public String getCode() {
    return this.code;
  }

  /**
   * Returns the error type associated with the SmartcarExceptionV2.
   *
   * @return the error type
   */
  public String getType() {
    return this.type;
  }

  /**
   * Returns the description associated with the exception.
   *
   * @return the description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Returns the resolution message associated with the exception.
   *
   * @return the resolution message
   */
  public String getResolution() {
    return this.resolution;
  }

  /**
   * Returns the documentation URL associated with the exception.
   *
   * @return the documentation URL
   */
  public String getDocURL() {
    return this.docURL;
  }
  
  /**
   * Returns the error details if available for this exception.
   *
   * @return the error details
   */
  public String getDetail() {
    return this.detail;
  }

  /**
   * Returns the HTTP status code.
   *
   * @return the response status code
   */
  public int getStatusCode() {
    return this.statusCode;
  }

  /**
   * Return the Smartcar request id from the response headers
   *
   * @return the request id
   */
  public String getRequestId() {
    return this.requestId;
  }

  public static SmartcarExceptionV2 Factory(final Response response) throws IOException {
    JsonObject body;
    String bodyString = response.body().string();
    System.out.println(bodyString);
    try {
      body = gson.fromJson(bodyString, JsonObject.class);
    } catch (com.google.gson.JsonParseException exception) {
      // In case the body is a string. Ex. gateway timeout where LB sends a non json body
      String errorBody = Json.createObjectBuilder()
              .add("description", bodyString)
              .build().toString();
      body = gson.fromJson(errorBody, JsonObject.class);
    } catch (Exception exception) {
      // Any other exception converting it to SmartcarExceptionV2
      String errorBody = Json.createObjectBuilder()
              .add("description", exception.getMessage())
              .build().toString();
      body = gson.fromJson(errorBody, JsonObject.class);
    }
    return gson.fromJson(body, SmartcarExceptionV2.class);
  }
}
