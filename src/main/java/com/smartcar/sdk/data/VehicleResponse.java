package com.smartcar.sdk.data;

import com.google.gson.JsonObject;

/** POJO for the Response object */
public class VehicleResponse extends ApiData {
  private JsonObject body;

  public VehicleResponse(JsonObject body) {
    this.body = body;
  }

  /**
   * Returns the body of the response
   *
   * @return body
   */
  public JsonObject getBody() {
    return this.body;
  }

  /**
   * Returns the body of the response as a Json String
   *
   * @return body as Json String
   */
  public String getBodyAsString() {
    return this.body.toString();
  }
}
