package com.smartcar.sdk.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartcar.sdk.SmartcarException;

import org.apache.commons.text.CaseUtils;
import org.joda.time.DateTime;

public class BatchResponse extends ApiData {
  private Map<String, JsonObject> responseData = new HashMap<>();
  private static GsonBuilder gson = new GsonBuilder().setFieldNamingStrategy((field) -> toCamelCase(field.getName()));

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

  public <T extends ApiData> SmartcarResponse<T> get(String path) throws SmartcarException {
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
    Class<T> dataType = getClassByPath(path);

    return createSmartcarResponse(body, header, dataType);
  }

  private <T extends ApiData> SmartcarResponse<T> createSmartcarResponse(JsonElement body, JsonElement header, Class<T> dataType) throws SmartcarException {
    T data = gson.create().fromJson(body, dataType);

    String unitHeader = header.getAsJsonObject().get("unitSystem").getAsString();
    String ageHeader;
    try {
      ageHeader = header.getAsJsonObject().get("dataAge").getAsString();
    } catch(Exception e){
      ageHeader = null;
   }

    if(ageHeader != null) {
      DateTime date = DateTime.parse(ageHeader);
      return new SmartcarResponse<T>(data, unitHeader, date.toDate());
    } else {
      return new SmartcarResponse<T>(data, unitHeader, null);
    }
  }

  private Class getClassByPath(String path) throws SmartcarException {
    switch (path) {
      case "/battery":
        return VehicleBattery.class;
      case "/charge":
        return VehicleCharge.class;
      case "/fuel":
        return VehicleFuel.class;
      case "/":
        return VehicleInfo.class;
      case "/location":
        return VehicleLocation.class;
      case "/odometer":
        return VehicleOdometer.class;
      case "/engine/oil":
        return VehicleOil.class;
      case "/tires/pressure":
        return VehicleTirePressure.class;
      case "/vin":
        return VehicleVin.class;
      default:
        throw new SmartcarException("Invalid path.");
      }
  }

  /**
   * @return a stringified representation of BatchResponse
   */
  @Override
  public String toString() {
    return responseData.toString();
  }
}
