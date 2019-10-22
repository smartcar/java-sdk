package com.smartcar.sdk.data;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.SmartcarException;

import org.apache.commons.text.CaseUtils;
import org.joda.time.DateTime;


/**
 * Smartcar BatchResponse Object
 */
public class BatchResponse extends ApiData {
    private Map<String, JsonObject> responseData = new HashMap<>();
    private static GsonBuilder gson = new GsonBuilder().setFieldNamingStrategy((field) -> toCamelCase(field.getName()));

    /**
     * Initializes a new BatchResponse.
     *
     * @param responses the array of Json response objects
     */
    public BatchResponse(JsonArray responses) {
        for (JsonElement response : responses) {
            JsonObject res = response.getAsJsonObject();
            String path = res.get("path").getAsString();
            this.responseData.put(path, res);
        }
    }

    private static String toCamelCase(String fieldName) {
        if (fieldName.contains("_")) { // checks for snake case
            return CaseUtils.toCamelCase(fieldName, false, '_');
        }
        return fieldName;
    }

    private <T extends ApiData> SmartcarResponse<T> get(String path, Class<T> dataType) throws SmartcarException {
        JsonObject res = this.responseData.get(path);
        if (res == null) {
            throw new SmartcarException("There is no response for the path: " + path);
        }
        int statusCode = res.get("code").getAsInt();
        JsonObject body = res.get("body").getAsJsonObject();

        if (statusCode != 200) {
            String message = body.get("message").getAsString();
            String error = body.get("error").getAsString();
            String code = "";
            if (body.has("code")) {
                code = body.get("code").getAsString();
            }

            throw new SmartcarException(statusCode, error, message, code);
        }

        JsonElement header = res.get("headers");

        return createSmartcarResponse(body, header, dataType);
    }

    private <T extends ApiData> SmartcarResponse<T> createSmartcarResponse(JsonElement body, JsonElement header, Class<T> dataType) throws SmartcarException {
        T data = gson.create().fromJson(body, dataType);

        String unitHeader = header.getAsJsonObject().get("unitSystem").getAsString();
        String ageHeader;
        try {
            ageHeader = header.getAsJsonObject().get("dataAge").getAsString();
        } catch (Exception e) {
            ageHeader = null;
        }

        if (ageHeader != null) {
            DateTime date = DateTime.parse(ageHeader);
            return new SmartcarResponse<T>(data, unitHeader, date.toDate());
        } else {
            return new SmartcarResponse<T>(data, unitHeader, null);
        }
    }

    /**
     * Send request to the /battery endpoint
     *
     * @return the battery status of the vehicle
     * @throws SmartcarException if the request is unsuccessful
     */
    public SmartcarResponse<VehicleBattery> battery() throws SmartcarException {
        return get("/battery", VehicleBattery.class);
    }

    /**
     * Send request to the /charge endpoint
     *
     * @return the charge status of the vehicle
     * @throws SmartcarException if the request is unsuccessful
     */
    public SmartcarResponse<VehicleCharge> charge() throws SmartcarException {
        return get("/charge", VehicleCharge.class);
    }

    /**
     * Send request to the /fuel endpoint
     *
     * @return the fuel status of the vehicle
     * @throws SmartcarException if the request is unsuccessful
     */
    public SmartcarResponse<VehicleFuel> fuel() throws SmartcarException {
        return get("/fuel", VehicleFuel.class);
    }

    /**
     * Send request to the /info endpoint
     *
     * @return VehicleInfo object
     * @throws SmartcarException if the request is unsuccessful
     */
    public VehicleInfo info() throws SmartcarException {
        return get("/", VehicleInfo.class).getData();
    }

    /**
     * Send request to the /vin endpoint
     *
     * @return the vin of the vehicle
     * @throws SmartcarException if the request is unsuccessful
     */
    public SmartcarResponse<VehicleLocation> location() throws SmartcarException {
        return get("/location", VehicleLocation.class);
    }

    /**
     * Send request to the /odometer endpoint
     *
     * @return the odometer of the vehicle
     * @throws SmartcarException if the request is unsuccessful
     */
    public SmartcarResponse<VehicleOdometer> odometer() throws SmartcarException {
        return get("/odometer", VehicleOdometer.class);
    }

    /**
     * Send request to the /engine/oil endpoint
     *
     * @return the engine oil status of the vehicle
     * @throws SmartcarException if the request is unsuccessful
     */
    public SmartcarResponse<VehicleOil> oil() throws SmartcarException {
        return get("/engine/oil", VehicleOil.class);
    }

    /**
     * Send request to the /vin endpoint
     *
     * @return the vin of the vehicle
     * @throws SmartcarException if the request is unsuccessful
     */
    public String vin() throws SmartcarException {
        return get("/vin", VehicleVin.class).getData().getVin();
    }

    /**
     * Send request to the /tires/pressure endpoint
     *
     * @return the tire pressure status of the vehicle
     * @throws SmartcarException if the request is unsuccessful
     */
    public SmartcarResponse<VehicleTirePressure> tirePressure() throws SmartcarException {
        return get("/tires/pressure", VehicleTirePressure.class);
    }

    /**
     * @return a stringified representation of BatchResponse
     */
    @Override
    public String toString() {
        return responseData.toString();
    }
}
