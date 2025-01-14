package com.smartcar.sdk.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.VehicleResponse;

import java.lang.reflect.Type;

/**
 * VehicleResponseDeserializer
 *
 * <p>This is a customer deserialization method to be used by the GSON object. By default, if you
 * pass a JSON object to GSON and tell it to turn that object into a class, it looks at the instance
 * variables on that class, and it attempts to assign vars from the JSON to this class.
 *
 * <p>But for the VehicleResponse class, we need to process that entire JSON response to the body field. This
 * method allows us to send the JSON data through the VehicleResponse constructor.
 */
public class VehicleResponseDeserializer implements JsonDeserializer<VehicleResponse> {
    public VehicleResponse deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject fullResponse = json.getAsJsonObject();
        return new VehicleResponse(fullResponse);
    }
}


