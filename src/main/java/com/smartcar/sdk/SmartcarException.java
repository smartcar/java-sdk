package com.smartcar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.json.Json;
import okhttp3.Response;

/**
 * Thrown when the Smartcar API library encounters a problem.
 */
public class SmartcarException extends java.lang.Exception {

  static Gson gson = new Gson();

  protected int statusCode;
  private String error;
  private String message;
  protected String code;
  protected String requestId;

  public SmartcarException(
      int statusCode, String error, String message, String code, String requestId) {
    super(message);
    this.message = message;
    this.code = code;
    this.error = error;
    this.statusCode = statusCode;
    this.requestId = requestId;
  }

  /**
   * Initializes a new Smartcar API exception with the specified message.
   *
   * @param message a message associated with the exception
   */
  public SmartcarException(final String message) {
    this.message = message;
  }

  /**
   * Returns the vehicle state error code associated with the SmartcarException.
   *
   * @return the vehicle state error code
   */
  public String getCode() {
    return this.code;
  }

  /**
   * Returns the message associated with the exception.
   *
   * @return the message
   */
  public String getMessage() {
    return this.message;
  }

  /**
   * Returns the error code associated with the exception.
   *
   * @return the error string code
   */
  public String getError() {
    return this.error;
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

  public static SmartcarException Factory(final Response response) throws IOException {
    JsonObject body;
    String bodyString = response.body().string();
    try {
      body = gson.fromJson(bodyString, JsonObject.class);
    } catch (com.google.gson.JsonParseException exception) {
      // In case the body is a string. Ex. gateway timeout where LB sends a non json body
      String errorBody = Json.createObjectBuilder().add("message", bodyString).build().toString();
      body = gson.fromJson(errorBody, JsonObject.class);
    } catch (Exception exception) {
      // Any other exception converting it to SmartcarException
      String errorBody =
          Json.createObjectBuilder().add("message", exception.getMessage()).build().toString();
      body = gson.fromJson(errorBody, JsonObject.class);
    }

    int statusCode = response.code();

    String code = "";
    if (body.has("code")) {
      code = body.get("code").getAsString();
    }

    String message = "";
    if (body.has("message")) {
      message = body.get("message").getAsString();
    } else if (body.has("error_description")) {
      message = body.get("error_description").getAsString();
    }

    String error = "";
    if (body.has("error")) {
      error = body.get("error").getAsString();
    }

    String requestId = response.header("sc-request-id", "");

    return new SmartcarException(statusCode, error, message, code, requestId);
  }
}
