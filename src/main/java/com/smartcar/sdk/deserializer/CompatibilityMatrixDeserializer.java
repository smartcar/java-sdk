package com.smartcar.sdk.deserializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.smartcar.sdk.data.CompatibilityMatrix;

public class CompatibilityMatrixDeserializer implements JsonDeserializer<CompatibilityMatrix> {
  @Override
  public CompatibilityMatrix deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    CompatibilityMatrix compatibilityMatrix = new CompatibilityMatrix();
    Set<String> makes = jsonObject.keySet();
    Map<String, List<CompatibilityMatrix.CompatibilityEntry>> makeCompatibilityMap = new HashMap<>(makes.size());
    makes.forEach(make -> {
      JsonElement makeElement = jsonObject.get(make);
      Type compatibilityEntriesType = new TypeToken<List<CompatibilityMatrix.CompatibilityEntry>>() {}.getType();
      List<CompatibilityMatrix.CompatibilityEntry> compatibilityEntries =
          context.deserialize(makeElement, compatibilityEntriesType);
      makeCompatibilityMap.put(make, compatibilityEntries);
    });

    compatibilityMatrix.setMakeCompatibilityMap(makeCompatibilityMap);
    return compatibilityMatrix;
  }

}
