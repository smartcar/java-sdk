package com.smartcar.sdk;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Thrown when the Smartcar API library encounters a problem.
 */
public class SmartcarException extends java.lang.Exception {
  public enum Status {
    UNKNOWN                 (0, "Unknown"),
    VALIDATION              (400, "Validation"),
    AUTHENTICATION          (401, "Authentication"),
    PERMISSION              (403, "Permission"),
    RESOURCE_NOT_FOUND      (404, "Resource Not Found"),
    VEHICLE_STATE           (409, "Vehicle State"),
    RATE_LIMITING           (429, "Rate Limiting"),
    MONTHLY_LIMIT_EXCEEDED  (430, "Monthly Limit Exceeded"),
    SERVER                  (500, "Server"),
    NOT_CAPABLE             (501, "Not Capable"),
    GATEWAY_TIMEOUT         (504, "Gateway Timeout");

    private final int code;
    private final String text;

    /**
     * Initializes a new Status enum.
     *
     * @param code the code representation
     * @param text the text representation
     */
    Status(int code, String text) {
      this.code = code;
      this.text = text;
    }

    /**
     * Returns the code representation.
     *
     * @return the code representation
     */
    public int getCode() {
      return this.code;
    }

    /**
     * Returns the text representation.
     *
     * @return the text representation
     */
    public String getText() {
      return this.text;
    }

    /**
     * Returns the corresponding Status enum for the specified code.
     *
     * @param code the code for the desired Status
     *
     * @return the matching Status
     *
     * @throws IllegalArgumentException if no matching Status is found
     */
    public static Status forCode(int code) throws IllegalArgumentException {
      for(Status s : values()) {
        if(s.code == code) {
          return s;
        }
      }

      return Status.UNKNOWN;
    }

    /**
     * Returns a string representation of the status.
     *
     * @return the string representation
     */
    public String toString() {
      return this.code + ": " + this.text;
    }
  }

  private Status status;
  private Request request;
  private Response response;

  /**
   * Initializes a new Smartcar API exception with default values.
   */
  public SmartcarException() {
    this(null, Status.UNKNOWN);
  }

  /**
   * Initializes a new Smartcar API exception with the specified message.
   *
   * @param message a message associated with the exception
   */
  public SmartcarException(final String message) {
    this(message, null);
  }

  /**
   * Initializes a new Smartcar API exception with the specified status.
   *
   * @param status a status associated with the exception
   */
  public SmartcarException(final Status status) {
    this(null, status);
  }

  /**
   * Initializes a new Smartcar API exception with the specified message and
   * status.
   *
   * @param message a message associated with the exception
   * @param status a status associated with the exception
   */
  public SmartcarException(final String message, final Status status) {
    this(message, status, null, null);
  }

  /**
   * Initializes a new Smartcar API exception with the specified message and
   * status.
   *
   * @param request the associated HTTP request
   * @param response the associated HTTP response
   */
  public SmartcarException(final Request request, final Response response) {
    this(null, Status.forCode(response.code()), request, response);
  }

  /**
   * Initializes a new Smartcar API exception with the specified status,
   * request, and response.
   *
   * @param status a status associated with the exception
   * @param request the associated HTTP request
   * @param response the associated HTTP response
   */
  public SmartcarException(final Status status, final Request request, final Response response) {
    this(null, status, request, response);
  }

  /**
   * Initializes a new Smartcar API exception with the specified message,
   * status, request, and response.
   *
   * @param message a message associated with the exception
   * @param status a status associated with the exception
   * @param request the associated HTTP request
   * @param response the associated HTTP response
   */
  public SmartcarException(final String message, final Status status, final Request request, final Response response) {
    super(SmartcarException.generateMessage(message, status));

    if(status != null) {
      this.status = status;
    }
    else {
      this.status = Status.UNKNOWN;
    }

    this.request = request;
    this.response = response;
  }

  /**
   * Generates a message for the exception, handling cases where no message is provided.
   *
   * @param message the message specified when the exception was thrown
   * @param status the status specified when the exception was thrown
   *
   * @return the generated message
   */
  private static String generateMessage(final String message, final Status status) {
    if(message != null) {
      return message;
    }

    if(status != null) {
      return status.toString();
    }

    return Status.UNKNOWN.toString();
  }

  /**
   * Returns the status associated with the SmartcarException.
   *
   * @return the associated status
   */
  public Status getStatus() {
    return this.status;
  }

  /**
   * Returns the HTTP request associated with the exception.
   *
   * @return the request object
   */
  public Request getRequest() {
    return this.request;
  }

  /**
   * Returns the HTTP response associated with the exception.
   *
   * @return the response object
   */
  public Response getResponse() {
    return this.response;
  }
}
