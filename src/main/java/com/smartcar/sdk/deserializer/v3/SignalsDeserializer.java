package com.smartcar.sdk.deserializer.v3;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.smartcar.sdk.data.v3.Signal;
import com.smartcar.sdk.data.v3.Signals;

public class SignalsDeserializer implements com.google.gson.JsonDeserializer<Signals> {
  @Override
  public Signals deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject root = json.getAsJsonObject();
    JsonElement signalsElement = root.get("data");
    root.add("data", new JsonArray()); // Temporarily remove data

    Signals result = new Signals();
    result.setResponse(root);

    Signal[] signals = context.deserialize(signalsElement, Signal[].class);
    result.setSignals(Arrays.asList(signals));
    result.setMeta(
        context.deserialize(json.getAsJsonObject().get("meta"), Signals.SignalsMeta.class));
    Map<String, String> links = context.deserialize(
        json.getAsJsonObject().get("links"), java.util.Map.class);
    result.setLinks(links);
    JsonElement includedEl = root.get("included");

    return result;
  }
}
