package com.smartcar.sdk;

import com.smartcar.sdk.data.ApiData;
import com.smartcar.sdk.data.ApplicationPermissions;
import com.smartcar.sdk.data.VehicleInfo;
import com.smartcar.sdk.data.VehicleVin;
import com.sun.istack.internal.Nullable;
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
  private <T extends ApiData> T call(String path, String method, @Nullable RequestBody body, Class<T> type) throws SmartcarException {
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
   * Executes an API request under the Vehicles endpoint.
   *
   * @param path the path to the sub-endpoint
   *
   * @return the response value
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  private String call(String path, String method, @Nullable RequestBody body) throws SmartcarException {
    return this.call(path, method, body, ApiData.class).toString();
  }

  /**
   * Send request to the /info endpoint
   *
   * @return VehicleInfo object
   */
  public VehicleInfo info() throws SmartcarException {
    return this.call("", "GET", null, VehicleInfo.class);
  }

  /**
   * Send request to the /vin endpoint
   *
   * @return the vin of the vehicle
   */
  public String vin() throws SmartcarException {
    VehicleVin vehicleVin = this.call("/vin", "GET", null, VehicleVin.class);
    return vehicleVin.getVin();
  }

  /**
   * Send request to the /permissions endpoint
   *
   * @return the permission of the application
   */
  public String[] permissions() throws SmartcarException {
    ApplicationPermissions appPermissions = this.call("/permissions", "GET", null, ApplicationPermissions.class);
    return appPermissions.getPermissions();
  }

  /**
   * Send request to the /disconnect endpoint
   */
  public void disconnect() throws SmartcarException {
    this.call("/disconnect", "DELETE", null);
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
