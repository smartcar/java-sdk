package com.smartcar.sdk.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class BatchDeserializer implements JsonDeserializer<BatchResponse> {
    public BatchResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject fullResponse = json.getAsJsonObject();
        JsonElement responsesElement = fullResponse.get("responses");
        return new BatchResponse(responsesElement.getAsJsonArray());
    }
  }
