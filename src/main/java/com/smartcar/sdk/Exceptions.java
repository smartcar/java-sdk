package com.smartcar.sdk;

public class Exceptions {
  static class SmartcarException extends Exception {
    public SmartcarException(String message) {
      super(message);
    }
  }

  static class ValidationException extends SmartcarException {
    public ValidationException(String message) {
      super(message);
    }
  }

  static class AuthenticationException extends SmartcarException {
    public AuthenticationException(String message) {
      super(message);
    }
  }

  static class PermissionException extends SmartcarException {
    public PermissionException(String message) {
      super(message);
    }
  }

  static class ResourceNotFoundException extends SmartcarException {
    public ResourceNotFoundException(String message) {
      super(message);
    }
  }

  static class StateException extends SmartcarException {
    public StateException(String message) {
      super(message);
    }
  }

  static class RateLimitingException extends SmartcarException {
    public RateLimitingException(String message) {
      super(message);
    }
  }

  static class MonthlyLimitExceeded extends SmartcarException {
    public MonthlyLimitExceeded(String message) {
      super(message);
    }
  }

  static class ServerException extends SmartcarException {
    public ServerException(String message) {
      super(message);
    }
  }

  static class NotCapableException extends SmartcarException {
    public NotCapableException(String message) {
      super(message);
    }
  }

  static class GatewayTimeoutException extends SmartcarException {
    public GatewayTimeoutException(String message) {
      super(message);
    }
  }
}