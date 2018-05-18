package com.smartcar.sdk;

import com.smartcar.sdk.data.*;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

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
  private <T extends ApiData> SmartcarResponse<T> call(String path, String method, RequestBody body, Class<T> type) throws SmartcarException {
    HttpUrl url = HttpUrl.parse(Vehicle.URL_API + "/vehicles/" + this.vehicleId).newBuilder()
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
  private String call(String path, String method, RequestBody body) throws SmartcarException {
    return this.call(path, method, body, ApiData.class).toString();
  }

  /**
   * Send request to the /info endpoint
   *
   * @return VehicleInfo object
   */
  public VehicleInfo info() throws SmartcarException {
    return this.call("", "GET", null, VehicleInfo.class).getData();
  }

  /**
   * Send request to the /vin endpoint
   *
   * @return the vin of the vehicle
   */
  public String vin() throws SmartcarException {
    return this.call("/vin", "GET", null, VehicleVin.class).getData().getVin();
  }

  /**
   * Send request to the /permissions endpoint
   *
   * @return the permission of the application
   */
  public String[] permissions() throws SmartcarException {
    return this.call("/permissions", "GET", null, ApplicationPermissions.class).getData().getPermissions();
  }

  /**
   * Send request to the /disconnect endpoint
   */
  public void disconnect() throws SmartcarException {
    this.call("/disconnect", "DELETE", null);
  }

  /**
   * Send request to the /odometer endpoint
   *
   * @return the odometer of the vehicle
   */
  public SmartcarResponse odometer() throws SmartcarException {
    return this.call("/odometer", "GET", null, VehicleOdometer.class);
  }

  /**
   * Send request to the /location endpoint
   *
   * @return the location of the vehicle
   */
  public SmartcarResponse location() throws SmartcarException {
    return this.call("/location", "GET", null, VehicleLocation.class);
  }

  /**
   * Send request to the /security endpoint to unlock a vehicle
   */
  public void unlock() throws SmartcarException {
    RequestBody body = new FormBody.Builder()
      .add("action", "UNLOCK")
      .build();

    this.call("/security", "POST", body);
  }

  /**
   * Send request to the /security endpoint to lock a vehicle
   */
  public void lock() throws SmartcarException {
    RequestBody body = new FormBody.Builder()
      .add("action", "LOCK")
      .build();

    this.call("/security", "POST", body);
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
