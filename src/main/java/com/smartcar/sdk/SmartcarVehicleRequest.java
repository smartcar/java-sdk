package com.smartcar.sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.*;
import okhttp3.RequestBody;

/** Class encompassing optional arguments for Smartcar Vehicle general purpose requests */
public final class SmartcarVehicleRequest {
  private final String method;
  private final String path;
  private final RequestBody body;
  private final Map<String, String> headers;
  private final String flags;

  public static class Builder {
    private String method;
    private String path;
    private final JsonObjectBuilder body;
    private final Map<String, String> headers;
    private final List<String> flags;

    public Builder() {
      this.method = "";
      this.path = "";
      this.body = Json.createObjectBuilder();
      this.headers = new HashMap<>();
      this.flags = new ArrayList<>();
    }

    public Builder method(String method) {
      this.method = method;
      return this;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder addHeader(String key, String value) {
      this.headers.put(key, value);
      return this;
    }

    public Builder addBodyParameter(String key, String value) {
      this.body.add(key, value);
      return this;
    }

    public Builder addBodyParameter(String key, JsonArray values) {
      this.body.add(key, values);
      return this;
    }

    public Builder addFlag(String key, String value) {
      this.flags.add(key + ":" + value);
      return this;
    }

    public Builder addFlag(String key, boolean value) {
      this.flags.add(key + ":" + value);
      return this;
    }

    public SmartcarVehicleRequest build() throws Exception {
      if (this.method == null || this.method.equals("")) {
        throw new Exception("method must be defined");
      }
      if (this.path == null) {
        throw new Exception("path must be defined");
      }
      return new SmartcarVehicleRequest(this);
    }
  }

  private SmartcarVehicleRequest(Builder builder) {
    this.method = builder.method;
    this.path = builder.path;

    JsonObject jsonBody = builder.body.build();

    this.body = jsonBody.isEmpty() ? null : RequestBody.create(ApiClient.JSON, jsonBody.toString());

    // Shallow clone of headers Map
    this.headers = (HashMap<String, String>) ((HashMap<String, String>) builder.headers).clone();

    if (builder.flags.size() > 0) {
      String[] flagStrings = builder.flags.toArray(new String[0]);
      this.flags = Utils.join(flagStrings, " ");
    } else {
      this.flags = null;
    }
  }

  public String getMethod() {
    return this.method;
  }

  public String getPath() {
    return this.path;
  }

  public RequestBody getBody() {
    return this.body;
  }

  public Map<String, String> getHeaders() {
    return this.headers;
  }

  public String getFlags() {
    return this.flags;
  }
}
