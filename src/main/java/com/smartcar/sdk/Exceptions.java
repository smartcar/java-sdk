package com.smartcar.sdk;

public class Exceptions {
  public static class SmartcarException extends Exception {
    public SmartcarException(String message) {
      super(message);
    }
  }

  public static class ValidationException extends SmartcarException {
    public ValidationException(String message) {
      super(message);
    }
  }

  public static class AuthenticationException extends SmartcarException {
    public AuthenticationException(String message) {
      super(message);
    }
  }

  public static class PermissionException extends SmartcarException {
    public PermissionException(String message) {
      super(message);
    }
  }

  public static class ResourceNotFoundException extends SmartcarException {
    public ResourceNotFoundException(String message) {
      super(message);
    }
  }

  public static class StateException extends SmartcarException {
    public StateException(String message) {
      super(message);
    }
  }

  public static class RateLimitingException extends SmartcarException {
    public RateLimitingException(String message) {
      super(message);
    }
  }

  public static class MonthlyLimitExceeded extends SmartcarException {
    public MonthlyLimitExceeded(String message) {
      super(message);
    }
  }

  public static class ServerException extends SmartcarException {
    public ServerException(String message) {
      super(message);
    }
  }

  public static class NotCapableException extends SmartcarException {
    public NotCapableException(String message) {
      super(message);
    }
  }

  public static class GatewayTimeoutException extends SmartcarException {
    public GatewayTimeoutException(String message) {
      super(message);
    }
  }
}