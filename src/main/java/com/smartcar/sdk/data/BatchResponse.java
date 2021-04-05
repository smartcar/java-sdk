package com.smartcar.sdk.data;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.BatchResponseMissingException;
import com.smartcar.sdk.SmartcarException;

import com.smartcar.sdk.SmartcarExceptionV2;
import org.apache.commons.text.CaseUtils;

/**
 * Smartcar BatchResponse Object
 */
public class BatchResponse extends ApiData {
    private String requestId;
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

    private <T extends ApiData> SmartcarResponse<T> get(String path, Class<T> dataType) throws BatchResponseMissingException, SmartcarException {
        JsonObject res = this.responseData.get(path);
        if (res == null) {
            throw new BatchResponseMissingException("There is no existing batch response for the path: " + path);
        }
        int statusCode = res.get("code").getAsInt();
        JsonObject body = res.get("body").getAsJsonObject();

        if (statusCode != 200) {
            if (body.has("description")) {
                // handle v2 error format
                String type = body.get("type").getAsString();
                String code = body.get("code").getAsString();
                String description = body.get("description").getAsString();
                JsonElement resolutionElement = body.get("resolution");
                String resolution = null;
                if (!resolutionElement.isJsonNull()) {
                    resolution = body.get("resolution").getAsString();
                }

                JsonElement detailElement = body.get("detail");
                String[] detail = new String[1];
                if (!detailElement.isJsonNull()) {
                    JsonArray detailJson = detailElement.getAsJsonArray();
                    detail = new String[detailJson.size()];
                    for (int i = 0; i < detailJson.size(); i++) {
                        detail[i] = detailJson.get(i).getAsString();
                    }
                }

                String docURL = null;
                JsonElement docURLElement = body.get("docURL");
                if (!docURLElement.isJsonNull()) {
                    docURL = docURLElement.getAsString();
                }
                throw new SmartcarExceptionV2(type, code, description, resolution, detail, docURL, statusCode, this.requestId);
            } else if (body.has("message")) {
                String message = body.get("message").getAsString();
                String error = body.get("error").getAsString();
                String code = "";
                if (body.has("code")) {
                    code = body.get("code").getAsString();
                }
                throw new SmartcarException(statusCode, error, message, code, this.requestId);
            } else {
                throw new SmartcarException("Unknown error response");
            }

        }

        JsonElement header = res.get("headers");

        return createSmartcarResponse(body, header, dataType);
    }

    private <T extends ApiData> SmartcarResponse<T> createSmartcarResponse(JsonElement body, JsonElement header, Class<T> dataType) throws SmartcarException {
        T data = gson.create().fromJson(body, dataType);

        SmartcarResponse<T> smartcarResponse = new SmartcarResponse<T>(data);
        smartcarResponse.setRequestId(this.requestId);

        try {
            String unitSystem = header.getAsJsonObject().get("sc-unit-system").getAsString();
            smartcarResponse.setUnitSystem(unitSystem);
        } catch (Exception e) {}

        try {
            String ageHeader = header.getAsJsonObject().get("sc-data-age").getAsString();
            if (ageHeader != null) {
              Instant age = Instant.parse(ageHeader);
              smartcarResponse.setAge(Date.from(age));
            }
        } catch (Exception e) {}

        return smartcarResponse;
    }

    /**
     * Return the Smartcar request id from the response headers
     *
     * @return the request id
     */
    public String getRequestId() {
      return this.requestId;
    }

    /**
     * Sets the Smartcar request id from the response headers
     *
     * @param requestId the request id
     */
    public void setRequestId(String requestId) {
      this.requestId = requestId;
    }

    /**
     * Get response from the /battery endpoint
     *
     * @return the battery status of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public SmartcarResponse<VehicleBattery> battery() throws BatchResponseMissingException, SmartcarException {
        return get("/battery", VehicleBattery.class);
    }

    /**
     * Get response from the battery/capacity endpoint
     *
     * @return the battery capacity of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public SmartcarResponse<VehicleBatteryCapacity> batteryCapacity() throws BatchResponseMissingException, SmartcarException {
        return get("/battery/capacity", VehicleBatteryCapacity.class);
    }

    /**
     * Get response from the /charge endpoint
     *
     * @return the charge status of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public SmartcarResponse<VehicleCharge> charge() throws BatchResponseMissingException, SmartcarException {
        return get("/charge", VehicleCharge.class);
    }

    /**
     * Get response from the /fuel endpoint
     *
     * @return the fuel status of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public SmartcarResponse<VehicleFuel> fuel() throws BatchResponseMissingException, SmartcarException {
        return get("/fuel", VehicleFuel.class);
    }

    /**
     * Get response from the /info endpoint
     *
     * @return VehicleInfo object
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint +returned an
     * HTTP error code
     */
    public VehicleInfo info() throws BatchResponseMissingException, SmartcarException {
        return get("/", VehicleInfo.class).getData();
    }

    /**
     * Get response from the /vin endpoint
     *
     * @return the vin of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public SmartcarResponse<VehicleLocation> location() throws BatchResponseMissingException, SmartcarException {
        return get("/location", VehicleLocation.class);
    }

    /**
     * Get response from the /odometer endpoint
     *
     * @return the odometer of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public SmartcarResponse<VehicleOdometer> odometer() throws BatchResponseMissingException, SmartcarException {
        return get("/odometer", VehicleOdometer.class);
    }

    /**
     * Get response from the /engine/oil endpoint
     *
     * @return the engine oil status of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public SmartcarResponse<VehicleOil> oil() throws BatchResponseMissingException, SmartcarException {
        return get("/engine/oil", VehicleOil.class);
    }

    /**
     * Get response from the /vin endpoint
     *
     * @return the vin of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public String vin() throws BatchResponseMissingException, SmartcarException {
        return get("/vin", VehicleVin.class).getData().getVin();
    }

    /**
     * Get response from the /tires/pressure endpoint
     *
     * @return the tire pressure status of the vehicle
     *
     * @throws BatchResponseMissingException if this endpoint was not
     * part of the batch response
     * @throws SmartcarException if the request for this endpoint returned an
     * HTTP error code
     */
    public SmartcarResponse<VehicleTirePressure> tirePressure() throws BatchResponseMissingException, SmartcarException {
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
