package com.smartcar.sdk.data;

import javax.json.JsonObject;

/** POJO for the Response object */
public class VehicleResponse extends ApiData {
    private JsonObject body;
    private Meta meta;

    /**
     * Returns the body of the response
     *
     * @return body
     */
    public JsonObject getBody() { return this.body; }
}
