package com.smartcar.sdk.data.v3;

import com.google.gson.JsonObject;

public class Signal extends JsonApiData {
  private String id;
  private String code;
  private String name;
  private String group;
  private JsonObject status;
  private JsonObject body;

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

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
}