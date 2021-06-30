package com.smartcar.sdk.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.SmartcarException;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.text.CaseUtils;

/** Smartcar BatchResponse Object */
public class BatchResponse extends ApiData {
  private String requestId;
  private Map<String, JsonObject> responseData = new HashMap<>();
  private static GsonBuilder gson =
      new GsonBuilder().setFieldNamingStrategy((field) -> toCamelCase(field.getName()));

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

  private <T extends ApiData> T get(String path, Class<T> dataType)
      throws SmartcarException {
    JsonObject res = this.responseData.get(path);
    if (res == null) {
      return null;
    }
    int statusCode = res.get("code").getAsInt();
    JsonObject body = res.get("body").getAsJsonObject();
    JsonObject headers = res.get("headers").getAsJsonObject();
    headers.addProperty("sc-request-id", this.requestId);

    if (statusCode != 200) {
      throw SmartcarException.Factory(statusCode, headers, body);
    }

    String bodyString = body.toString();
    T data = gson.create().fromJson(bodyString, dataType);
    Meta meta = gson.create().fromJson(res.get("headers").getAsJsonObject().toString(), Meta.class);
    data.setMeta(meta);

    return data;
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
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleBattery battery()
      throws SmartcarException {
    return get("/battery", VehicleBattery.class);
  }

  /**
   * Get response from the battery/capacity endpoint
   *
   * @return the battery capacity of the vehicle
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleBatteryCapacity batteryCapacity()
      throws SmartcarException {
    return get("/battery/capacity", VehicleBatteryCapacity.class);
  }

  /**
   * Get response from the /charge endpoint
   *
   * @return the charge status of the vehicle
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleCharge charge()
      throws SmartcarException {
    return get("/charge", VehicleCharge.class);
  }

  /**
   * Get response from the /fuel endpoint
   *
   * @return the fuel status of the vehicle
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleFuel fuel()
      throws SmartcarException {
    return get("/fuel", VehicleFuel.class);
  }

  /**
   * Get response from the /info endpoint
   *
   * @return VehicleInfo object
   * @throws SmartcarException if the request for this endpoint +returned an HTTP error code
   */
  public VehicleAttributes info() throws SmartcarException {
    return get("/", VehicleAttributes.class);
  }

  /**
   * Get response from the /vin endpoint
   *
   * @return the vin of the vehicle
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleLocation location()
      throws SmartcarException {
    return get("/location", VehicleLocation.class);
  }

  /**
   * Get response from the /odometer endpoint
   *
   * @return the odometer of the vehicle
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleOdometer odometer()
      throws SmartcarException {
    return get("/odometer", VehicleOdometer.class);
  }

  /**
   * Get response from the /engine/oil endpoint
   *
   * @return the engine oil status of the vehicle
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleEngineOil oil()
      throws SmartcarException {
    return get("/engine/oil", VehicleEngineOil.class);
  }

  /**
   * Get response from the /vin endpoint
   *
   * @return the vin of the vehicle
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleVin vin() throws SmartcarException {
    return get("/vin", VehicleVin.class);
  }

  /**
   * Get response from the /tires/pressure endpoint
   *
   * @return the tire pressure status of the vehicle
   * @throws SmartcarException if the request for this endpoint returned an HTTP error code
   */
  public VehicleTirePressure tirePressure()
      throws SmartcarException {
    return get("/tires/pressure", VehicleTirePressure.class);
  }

  /** @return a stringified representation of BatchResponse */
  @Override
  public String toString() {
    return responseData.toString();
  }
}
