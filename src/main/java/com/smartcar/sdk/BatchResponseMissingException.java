package com.smartcar.sdk;

/** Thrown when a BatchResponse does not have data for an endpoint. */
public class BatchResponseMissingException extends SmartcarException {

  /**
   * Initializes a new Smartcar API exception with the specified message.
   *
   * @param message a message associated with the exception
   */
  public BatchResponseMissingException(final String message) {
    super(message);
  }
}
