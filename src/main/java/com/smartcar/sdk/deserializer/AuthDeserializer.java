package com.smartcar.sdk.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.data.Auth;

import java.lang.reflect.Type;
import java.util.Calendar;

/** Custom deserializer for Auth data from the OAuth endpoint. */
public class AuthDeserializer implements JsonDeserializer<Auth> {
    /**
     * Deserializes the OAuth auth endpoint JSON into a new Auth object.
     *
     * @param json the Json data being deserialized
     * @param typeOfT the type of the Object to deserialize to
     * @param context the deserialization context
     * @return the newly created Auth object
     */
    public Auth deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();

        // Get timestamp for expiration.
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.SECOND, jsonObject.get("expires_in").getAsInt());

        Calendar refreshExpiration = Calendar.getInstance();
        refreshExpiration.add(Calendar.DAY_OF_YEAR, 60);

        return new Auth(
                jsonObject.get("access_token").getAsString(),
                jsonObject.get("refresh_token").getAsString(),
                expiration.getTime(),
                refreshExpiration.getTime());
    }
}