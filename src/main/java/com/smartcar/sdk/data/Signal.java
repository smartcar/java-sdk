package com.smartcar.sdk.data;

import java.util.Map;

import com.google.gson.JsonObject;

public class Signal extends ApiData {
  public class SignalAttributes {
    private String code;
    private String name;
    private String group;
    private JsonObject status;
    private JsonObject body;
    private Map<String, String> links;

    public String getCode() {
      return this.code;
    }

    public String getName() {
      return this.name;
    }

    public String getGroup() {
      return this.group;
    }

    public JsonObject getStatus() {
      return this.status;
    }

    public JsonObject getBody() {
      return this.body;
    }

    public Map<String, String> getLinks() {
      return this.links;
    }
  }

  private SignalAttributes attributes;
  private Map<String, String> links;

  public SignalAttributes getAttributes() {
    return this.attributes;
  }

  public Map<String, String> getLinks() {
    return this.links;
  }
}
