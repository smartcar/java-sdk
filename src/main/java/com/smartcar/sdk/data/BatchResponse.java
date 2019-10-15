package com.smartcar.sdk.data;

import com.smartcar.sdk.SmartcarException;

import org.joda.time.DateTime;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import org.apache.commons.text.CaseUtils;
import java.util.HashMap;
import java.util.Iterator;

public class BatchResponse extends ApiData {
  private HashMap<String, JsonObject> responseData = new HashMap<>();

  public BatchResponse(JsonArray responses) {

    Iterator<JsonElement> responseIterator = responses.iterator();

    while (responseIterator.hasNext()){
      JsonObject res = responseIterator.next().getAsJsonObject();
      String path = res.get("path").getAsString();
      this.responseData.put(path, res);
    }
  }


  private static String toCamelCase(String fieldName) {
    if (fieldName.contains("_")) { // checks for snake case
      return CaseUtils.toCamelCase(fieldName, false, new char[]{'_'});
    }
    return fieldName;
  }

  static GsonBuilder gson = new GsonBuilder().setFieldNamingStrategy((field) -> toCamelCase(field.getName()));


  public <T extends ApiData> SmartcarResponse<T> get(String path) throws SmartcarException {
    JsonObject res = this.responseData.get(path);

    Integer code = res.get("code").getAsInt();
    if (code != 200) {
      // TODO : handle error
      System.out.println("ERROR");
    }
    JsonObject body = res.get("body").getAsJsonObject();
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

  public <T extends ApiData> Class<T> getClassByPath(String path) throws SmartcarException {
    switch (path) {
      case "/battery":
        return (Class<T>) VehicleBattery.class;
      case "/charge":
        return (Class<T>) VehicleCharge.class;
      case "/fuel":
        return (Class<T>) VehicleFuel.class;
      case "/":
        return (Class<T>) VehicleInfo.class;
      case "/location":
        return (Class<T>) VehicleLocation.class;
      case "/odometer":
        return (Class<T>) VehicleOdometer.class;
      case "/engine/oil":
        return (Class<T>) VehicleOil.class;
      case "/tires/pressure":
        return (Class<T>) VehicleTirePressure.class;
      case "/vin":
        return (Class<T>) VehicleVin.class;
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
