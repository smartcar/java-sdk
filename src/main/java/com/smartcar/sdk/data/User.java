package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;
import com.smartcar.sdk.data.Meta;
import com.google.gson.JsonObject;
import okhttp3.Response;

import javax.json.Json;

public class User extends ApiData {
    private String id;

    /**
     * Returns the user id
     *
     * @return user id
     */
    public String getId() {
        return this.id;
    }

    /** @return a stringified representation of VehicleVin */
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "id='" + this.id + '\'' + '}';
        }
}
