package com.smartcar.sdk;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class encompassing optional arguments for Smartcar compatibility matrix requests
 */
public final class SmartcarCompatibilityMatrixRequest {
  private final String clientId;
  private final String clientSecret;
  private final String make;
  private final String mode;
  private final String region;
  private final String[] scope;
  private final String type;
  private final String version;

  public static class Builder {
    private String clientId;
    private String clientSecret;
    private String make;
    private String mode;
    private String region;
    private List<String> scope;
    private String type;
    private String version;

    public Builder() {
      this.clientId = System.getenv("SMARTCAR_CLIENT_ID");
      this.clientSecret = System.getenv("SMARTCAR_CLIENT_SECRET");
      this.mode = null;
      this.make = null;
      this.region = "US";
      this.scope = null;
      this.type = null;
      this.version = Smartcar.API_VERSION;
    }

    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    public Builder clientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
      return this;
    }

    public Builder make(String make) {
      this.make = make;
      return this;
    }

    public Builder mode(String mode) throws Exception {
      Utils.validateMode(mode);
      this.mode = mode;
      return this;
    }

    public Builder region(String region) {
      this.region = region;
      return this;
    }

    public Builder scope(String[] scope) {
      this.scope = Arrays.asList(scope);
      return this;
    }

    public Builder scope(Collection<String> scope) {
      this.scope = scope.stream().collect(Collectors.toList());
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder version(String version) {
      this.version = version;
      return this;
    }

    public SmartcarCompatibilityMatrixRequest build() {
      return new SmartcarCompatibilityMatrixRequest(this);
    }
  }

  private SmartcarCompatibilityMatrixRequest(Builder builder) {
    this.clientId = builder.clientId;
    this.clientSecret = builder.clientSecret;
    this.make = builder.make;
    this.mode = builder.mode;
    this.region = builder.region;
    this.scope = builder.scope != null ? builder.scope.toArray(new String[0]) : null;
    this.type = builder.type;
    this.version = builder.version;
  }

  public String getClientId() {
    return this.clientId;
  }

  public String getClientSecret() {
    return this.clientSecret;
  }

  public String getMake() {
    return this.make;
  }

  public String getMode() {
    return this.mode;
  }

  public String getRegion() {
    return this.region;
  }

  public String[] getScope() {
    return this.scope;
  }

  public String getType() {
    return this.type;
  }

  public String getVersion() {
    return this.version;
  }
}
