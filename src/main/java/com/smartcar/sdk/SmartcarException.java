package com.smartcar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.Headers;
import okhttp3.ResponseBody;

import java.io.IOException;

/** Thrown when the Smartcar API library encounters a problem. */
public class SmartcarException extends java.lang.Exception {

  private final int statusCode;
  private final String type;
  private final String code;
  private final String description;
  private final String resolutionType;
  private final String resolutionUrl;
  private final JsonArray detail;
  private final String docURL;
  private final String requestId;

  public static class Builder {
    private int statusCode;
    private String type;
    private String code;
    private String description;
    private String resolutionType;
    private String resolutionUrl;
    private JsonArray detail;
    private String docURL;
    private String requestId;

    public Builder() {
      this.statusCode = 0;
      this.type = "";
      this.code = null;
      this.description = "";
      this.resolutionType = null;
      this.resolutionUrl = null;
      this.detail = null;
      this.docURL = "";
      this.requestId = "";
    }

    public Builder statusCode(int statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder resolutionType(String resolutionType) {
      this.resolutionType = resolutionType;
      return this;
    }

    public Builder resolutionUrl(String resolutionUrl) {
      this.resolutionUrl = resolutionUrl;
      return this;
    }

    public Builder detail(JsonArray detail) {
      this.detail = detail;
      return this;
    }

    public Builder docURL(String docURL) {
      this.docURL = docURL;
      return this;
    }

    public Builder requestId(String requestId) {
      this.requestId = requestId;
      return this;
    }

    public SmartcarException build() { return new SmartcarException(this); }
  }

  private SmartcarException(Builder builder) {
    this.statusCode = builder.statusCode;
    this.type = builder.type;
    this.code = builder.code;
    this.description = builder.description;
    this.resolutionUrl = builder.resolutionUrl;
    this.resolutionType = builder.resolutionType;
    this.detail = builder.detail;
    this.docURL = builder.docURL;
    this.requestId = builder.requestId;
  }

  public static SmartcarException Factory(final int statusCode, JsonObject headers, JsonObject body) {
    Builder builder = new SmartcarException.Builder().statusCode(statusCode);

    JsonElement requestId = headers.get("sc-request-id");
    if (requestId != null) {
      builder.requestId(requestId.getAsString());
    }
    JsonElement contentType = headers.get("content-type");
    if (contentType != null && !contentType.getAsString().contains("application/json")) {
      return builder.description(body.toString()).build();
    }

    String bodyString = null;
    try {
      bodyString = body.toString();
    } catch (NullPointerException ex) {
      return builder
              .description("Empty response body")
              .type("SDK_ERROR")
              .build();
    }
    System.out.println(bodyString);

    JsonObject bodyJson = new Gson().fromJson(bodyString, JsonObject.class);
    if (bodyJson.has("error")) {
      builder.type(bodyJson.get("error").getAsString());
      if (bodyJson.has("message")) {
        builder.description(bodyJson.get("message").getAsString());
      }
      if (bodyJson.has("error_description")) {
        builder.description(bodyJson.get("error_description").getAsString());
      }
      if (bodyJson.has("code")) {
        builder.code(bodyJson.get("code").getAsString());
      }
      return builder.build();
    } else if (bodyJson.has("type")) {
      builder
              .type(bodyJson.get("type").getAsString())
              .description(bodyJson.get("description").getAsString())
              .docURL(bodyJson.get("docURL").getAsString());

      if (!bodyJson.get("code").isJsonNull()) {
        builder.code(bodyJson.get("code").getAsString());
      }

      JsonElement resolutionElement = bodyJson.get("resolution");
      if (!resolutionElement.isJsonNull()) {
        if (resolutionElement.isJsonPrimitive() && resolutionElement.getAsJsonPrimitive().isString()) {
          builder.resolutionType(resolutionElement.getAsString());
        } else {
          JsonObject resolution = resolutionElement.getAsJsonObject();
          JsonElement type = resolution.get("type");
          if (!type.isJsonNull()) {
            builder.resolutionType(type.getAsString());
          }
          JsonElement url = resolution.get("url");
          if (url != null) {
            builder.resolutionUrl(url.getAsString());
          }
        }
      }

      if (bodyJson.has("detail")) {
        JsonArray detailJson = bodyJson.get("detail").getAsJsonArray();
        builder.detail(detailJson);
      }

      return builder.build();
    }

    return builder
            .description(bodyString)
            .type("SDK_ERROR")
            .build();
  }

  public static SmartcarException Factory(final int statusCode, Headers headers, ResponseBody body) {
    JsonObject headerJson = new JsonObject();
    for (String header: headers.names()) {
      headerJson.addProperty(header, headers.get(header));
    }

    JsonObject bodyJson = null;
    String bodyString = null;
    try {
      bodyString = body.string();
    } catch (IOException e) {
      return new SmartcarException.Builder()
              .statusCode(statusCode)
              .description("Unable to get request body")
              .requestId(headers.get("sc-request-id"))
              .type("SDK_ERROR")
              .build();
    }
    try {
      bodyJson = new Gson().fromJson(bodyString, JsonObject.class);
    } catch (Exception e) {
      // Handles non 200 invalid JSON errors
      return new SmartcarException.Builder()
              .statusCode(statusCode)
              .description(bodyString)
              .requestId(headers.get("sc-request-id"))
              .type("SDK_ERROR")
              .build();
    }

    return SmartcarException.Factory(statusCode, headerJson, bodyJson);
  }

  /**
   * Returns the error message
   *
   * @return message
   */
  public String getMessage() {
    if (this.type != null) {
      return this.type + ":" + this.code + " - " + this.description;
    }
    return this.description;
  }

  public int getStatusCode() { return this.statusCode; }

  public String getRequestId() { return this.requestId; }

  /**
   * Returns the error type associated with the SmartcarExceptionV2.
   *
   * @return the error type
   */
  public String getType() {
    return this.type;
  }

  public String getCode() { return this.code; }

  /**
   * Returns the description associated with the exception.
   *
   * @return the description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Returns the resolution type associated with the exception.
   *
   * @return the resolution type
   */
  public String getResolutionType() {
    return this.resolutionType;
  }

  /**
   * Returns a url associated with the resolution to the exception
   *
   * @return the resolution url
   */
  public String getResolutionUrl() { return this.resolutionUrl; }

  /**
   * Returns the documentation URL associated with the exception.
   *
   * @return the documentation URL
   */
  public String getDocURL() {
    return this.docURL;
  }

  /**
   * Returns the error details if available for this exception.
   *
   * @return the error details
   */
  public JsonArray getDetail() {
    return this.detail;
  }

}
