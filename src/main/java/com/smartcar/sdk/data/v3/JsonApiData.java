package com.smartcar.sdk.data.v3;

import java.util.Collections;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.ApiData;

public class JsonApiData<T> extends ApiData<T> {
  JsonObject response;
  Map<String, String> links;

  public JsonObject getResponse() {
    return response;
  }

  public void setResponse(JsonObject response) {
    this.response = response;
  }

  public JsonElement getIncluded() {
    return response.get("included");
  }

  public Map<String, String> getLinks() {
    if (this.links == null) {
      return Collections.emptyMap();
    }
    return links;
  }

  public void setLinks(Map<String, String> links) {
    this.links = links;
  }
}
