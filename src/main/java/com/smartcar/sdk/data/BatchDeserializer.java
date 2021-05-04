package com.smartcar.sdk.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

/**
 * BatchDeserializer
 *
 * This is a customer deserialization method to be used by the GSON object.
 * By default, if you pass a JSON object to GSON and tell it to turn that
 * object into a class, it looks at the instance variables on that class, and
 * it attempts to assign vars from the JSON to this class.
 *
 * But for the BatchResponse class, we need to process that JSON response
 * from /batch. This method allows us to send the JSON data through the
 * BatchResponse constructor.
 *
 */
public class BatchDeserializer implements JsonDeserializer<BatchResponse> {
  public BatchResponse deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    JsonObject fullResponse = json.getAsJsonObject();
    JsonElement responsesElement = fullResponse.get("responses");
    return new BatchResponse(responsesElement.getAsJsonArray());
  }
}
