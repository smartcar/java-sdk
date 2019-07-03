package com.smartcar.sdk;

import com.smartcar.sdk.data.*;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Smartcar Vehicle API Object
 */
public class Vehicle extends ApiClient {
  public enum UnitSystem {
    DEFAULT,
    IMPERIAL,
    METRIC,
  }

  private String vehicleId;
  private String accessToken;
  private Vehicle.UnitSystem unitSystem = UnitSystem.DEFAULT;

  /**
   * Initializes a new Vehicle.
   *
   * @param vehicleId the vehicle ID
   * @param accessToken the OAuth 2.0 access token
   * @param unitSystem the preferred unit system
   */
  public Vehicle(String vehicleId, String accessToken, Vehicle.UnitSystem unitSystem) {
    this.vehicleId = vehicleId;
    this.accessToken = accessToken;

    if(unitSystem != null) {
      this.unitSystem = unitSystem;
    }
  }

  /**
   * Initializes a new Vehicle.
   *
   * @param vehicleId the vehicle ID
   * @param accessToken the OAuth 2.0 access token
   */
  public Vehicle(String vehicleId, String accessToken) {
    this(vehicleId, accessToken, null);
  }

  /**
   * Executes an API request under the VehicleIds endpoint.
   *
   * @param path the path to the sub-endpoint
   * @param method the method of the request
   * @param body the body of the request
   * @param type the type into which the response will be parsed
   *
   * @return the parsed response
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  protected <T extends ApiData> SmartcarResponse<T> call(String path, String method, RequestBody body, Class<T> type) throws SmartcarException {
    HttpUrl url = HttpUrl.parse(Vehicle.URL_API).newBuilder()
        // addPathSegments will take care of adding leading `/`
        .addPathSegments("vehicles")
        .addPathSegments(this.vehicleId)
        .addPathSegments(path)
        .build();

    Request request = new Request.Builder()
        .url(url)
        .header("Authorization", "Bearer " + this.accessToken)
        .addHeader("User-Agent", Vehicle.USER_AGENT)
        .method(method, body)
        .build();

    return Vehicle.execute(request, type);
  }

  /**
   * Executes an API request under the VehicleIds endpoint.
   *
   * @param path the path to the sub-endpoint
   * @param method the method of the request
   * @param body the body of the request
   *
   * @return the response value
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  protected String call(String path, String method, RequestBody body) throws SmartcarException {
    return this.call(path, method, body, ApiData.class).toString();
  }

  /**
   * Send request to the /info endpoint
   *
   * @return VehicleInfo object
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleInfo info() throws SmartcarException {
    return this.call("", "GET", null, VehicleInfo.class).getData();
  }

  /**
   * Send request to the /vin endpoint
   *
   * @return the vin of the vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public String vin() throws SmartcarException {
    return this.call("vin", "GET", null, VehicleVin.class).getData().getVin();
  }

  /**
   * Send request to the /permissions endpoint
   *
   * @return the permission of the application
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public String[] permissions() throws SmartcarException {
    return this.call("permissions", "GET", null, ApplicationPermissions.class).getData().getPermissions();
  }


  /**
   * Send request to the /permissions endpoint
   *
   * @param permission the permission to check on the vehicle
   *
   * @return if vehicle has specified permission
   * @throws SmartcarException if the request is unsuccessful
   */
  public boolean hasPermissions(String permission) throws SmartcarException {
    try {
      for(String vehiclePermission: this.permissions()){
        if(vehiclePermission.equals(permission))
          return true;
      }

      return false;
    } catch (SmartcarException exception) {
      throw exception;
    }
  }

  /**
   * Send request to the /permissions endpoint
   *
   * @param permissions the permissions to check on the vehicle
   *
   * @return if vehicle has specified permissions
   * @throws SmartcarException if the request is unsuccessful
   */
  public boolean hasPermissions(String[] permissions) throws SmartcarException {
    try {
      int commonPermissions = 0;

      for(String permission: permissions){
        for(String vehiclePermission: this.permissions()) {
          if (permission.equals(vehiclePermission)) {
            commonPermissions++;
            break;
          }
        }
      }

      return(commonPermissions == permissions.length) ? true : false;
    } catch (SmartcarException exception) {
      throw exception;
    }
  }

  /**
   * Send request to the /disconnect endpoint
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public void disconnect() throws SmartcarException {
    this.call("application", "DELETE", null);
  }

  /**
   * Send request to the /odometer endpoint
   *
   * @return the odometer of the vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public SmartcarResponse<VehicleOdometer> odometer() throws SmartcarException {
    return this.call("odometer", "GET", null, VehicleOdometer.class);
  }

  /**
   * Send request to the /location endpoint
   *
   * @return the location of the vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public SmartcarResponse<VehicleLocation> location() throws SmartcarException {
    return this.call("location", "GET", null, VehicleLocation.class);
  }

  /**
   * Send request to the /security endpoint to unlock a vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public void unlock() throws SmartcarException {
    JsonObject json = Json.createObjectBuilder()
      .add("action", "UNLOCK")
      .build();

    RequestBody body = RequestBody.create(JSON, json.toString());

    this.call("security", "POST", body);
  }

  /**
   * Send request to the /security endpoint to lock a vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public void lock() throws SmartcarException {
    JsonObject json = Json.createObjectBuilder()
      .add("action", "LOCK")
      .build();

    RequestBody body = RequestBody.create(JSON, json.toString());

    this.call("security", "POST", body);
  }

  /**
   * Returns the currently stored vehicle ID.
   *
   * @return the vehicle ID
   */
  public String getVehicleId() {
    return this.vehicleId;
  }

  /**
   * Stores the vehicleId
   *
   * @param vehicleId the vehicle ID
   */
  public void setVehicleId(String vehicleId) {
    this.vehicleId = vehicleId;
  }

  /**
   * Returns the currently stored access token.
   *
   * @return the access token
   */
  public String getAccessToken() {
    return this.accessToken;
  }

  /**
   * Stores the accessToken
   *
   * @param accessToken the accessToken
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * Returns the currently set unit system.
   *
   * @return the unit system
   */
  public Vehicle.UnitSystem getUnitSystem() {
    return this.unitSystem;
  }

  /**
   * Sets the preferred unit system for subsequent API requests.
   *
   * @param unitSystem the desired unit system
   */
  public void setUnitSystem(Vehicle.UnitSystem unitSystem) {
    this.unitSystem = unitSystem;
  }
}
