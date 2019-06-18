package com.smartcar.sdk;

import com.google.gson.JsonElement;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * Thrown when the Smartcar API library encounters a problem.
 */
public class SmartcarException extends java.lang.Exception {

  static Gson gson = new Gson();

  private Integer statusCode;
  private String error;
  private String message;
  private String code;

  public SmartcarException(Integer statusCode, String error, String message, String code) {
    super(message);
    this.message = message;
    this.code = code;
    this.error = error;
    this.statusCode = statusCode;
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
  public Integer getStatusCode() {
    return this.statusCode;
  }

  public static SmartcarException Factory(final Response response) throws IOException {
    JsonObject body = gson.fromJson(response.body().string(), JsonObject.class);

    String code = "";
    JsonElement codeElement = body.get("code");
    if (codeElement != null) {
      code = codeElement.getAsString();
    }
    String message = body.get("message").getAsString();
    String error = body.get("error").getAsString();
    Integer statusCode = response.code();
    return new SmartcarException(statusCode, error, message, code);
  }
}
