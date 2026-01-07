package com.smartcar.sdk.deserializer;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.smartcar.sdk.data.Signal;
import com.smartcar.sdk.data.Signals;

public class SignalsDeserializer implements JsonDeserializer<Signals> {
  @Override
  public Signals deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    Signals result = new Signals();
    Signal[] signals = context.deserialize(json.getAsJsonObject().get("data"), Signal[].class);
    result.setSignals(Arrays.asList(signals));
    result.setIncluded(json.getAsJsonObject().get("included").getAsJsonObject());
    result.setLinks(context.deserialize(json.getAsJsonObject().get("links"), Map.class));
    result.setMeta(
        context.deserialize(json.getAsJsonObject().get("meta"), Signals.SignalsMeta.class));
    return result;
  }
}
