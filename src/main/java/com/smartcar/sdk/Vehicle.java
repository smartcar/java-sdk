package com.smartcar.sdk;

import okhttp3.*;

/**
 * Smartcar Vehicle API Object
 */
public class Vehicle extends ApiClient {
  public enum UnitSystem {
    DEFAULT,
    IMPERIAL,
    METRIC
  }

  private static final String URL_API = "https://api.smartcar.com/";

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
   * Executes an API request under the Vehicles endpoint.
   *
   * @param path the path to the sub-endpoint
   * @param type the type into which the response will be parsed
   *
   * @return the parsed response
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  private <T extends ApiData> T call(String path, Class<T> type) throws SmartcarException {
    HttpUrl url = HttpUrl.parse(Vehicle.URL_API + "/vehicles/" + this.vehicleId).newBuilder()
        .addPathSegments(path)
        .build();
    Request request = new Request.Builder()
        .url(url)
        .header("Authorization", "Bearer " + this.accessToken)
        .addHeader("User-Agent", Vehicle.USER_AGENT)
        .build();

    return Vehicle.execute(request, type);
  }

  /**
   * Executes an API request under the Vehicles endpoint.
   *
   * @param path the path to the sub-endpoint
   *
   * @return the response value
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  private String call(String path) throws SmartcarException {
    return this.call(path, ApiData.class).toString();
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
   * Returns the currently stored access token.
   *
   * @return the access token
   */
  public String getAccessToken() {
    return this.accessToken;
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
