package com.smartcar.sdk.deserializer.v3;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.smartcar.sdk.data.Meta;
import com.smartcar.sdk.data.v3.JsonApiData;

public class JsonApiDeserializer<T extends JsonApiData> implements JsonDeserializer<T> {
  private static final Gson DEFAULT_GSON = new Gson();

  @Override
  public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject root = json.getAsJsonObject();
    // JsonObject dataElement = root.getAsJsonObject("data");
    JsonObject attributesElement = root.getAsJsonObject("attributes");

    // Use default Gson deserialization for the attributes to avoid
    // looping back into this deserializer
    T result = DEFAULT_GSON.fromJson(attributesElement, typeOfT);

    if (result == null) {
      return null;
    }

    result.setResponse(root);

    JsonElement idEl = root.get("id");
    if (idEl != null) {
      try {
        Method setIdMethod = result.getClass()
              .getMethod("setId", String.class);
        if (setIdMethod != null) {
          setIdMethod.invoke(result, idEl.getAsString());
        }
      } catch (Exception e) {
        throw new JsonParseException("Failed to set id on deserialized object", e);
      }
    }

    JsonElement linksEl = root.get("links");
    if (linksEl != null) {
      try {
        Map<String, String> links = context.deserialize(linksEl, java.util.Map.class);
        result.getClass()
              .getMethod("setLinks", Map.class)
              .invoke(result, links);
      } catch (Exception e) {
        throw new JsonParseException("Failed to set links on deserialized object", e);
      }
    }

    JsonElement metaEl = root.get("meta");
    if (metaEl != null) {
      try {
        Meta meta = context.deserialize(metaEl, Meta.class);
        result.getClass()
              .getMethod("setMeta", Meta.class)
              .invoke(result, meta);
      } catch (Exception e) {
        throw new JsonParseException("Failed to set meta on deserialized object", e);
      }
    }

    return result;
  }
}
