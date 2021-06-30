package com.smartcar.sdk;

import com.smartcar.sdk.data.*;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.HashMap;
import java.util.Map;

/** Smartcar Vehicle API Object */
public class Vehicle {
  public enum UnitSystem {
    IMPERIAL,
    METRIC,
  }

  private String vehicleId;
  private String accessToken;
  private Vehicle.UnitSystem unitSystem = UnitSystem.METRIC;
  private String version;
  private String origin;
  private ApplicationPermissions permissions;

  /**
   * Initializes a new Vehicle.
   *
   * @param vehicleId the vehicle ID
   * @param accessToken the OAuth 2.0 access token
   */
  public Vehicle(String vehicleId, String accessToken) {
    this(vehicleId, accessToken, new SmartcarVehicleOptions.Builder().build());
  }

  public Vehicle(String vehicleId, String accessToken, SmartcarVehicleOptions options) {
    this.vehicleId = vehicleId;
    this.accessToken = accessToken;
    this.version = options.getVersion();
    this.unitSystem = options.getUnitSystem();
    this.origin = options.getOrigin();
  }

  /**
   * Executes an API request under the VehicleIds endpoint.
   *
   * @param path the path to the sub-endpoint
   * @param method the method of the request
   * @param body the body of the request
   * @param type the type into which the response will be parsed
   * @return the parsed response
   * @throws SmartcarException if the request is unsuccessful
   */
  protected <T extends ApiData> T call(
      String path, String method, RequestBody body, String accessToken, Class<T> type) throws SmartcarException {
    HttpUrl url =
        HttpUrl.parse(this.origin)
            .newBuilder().addPathSegments("v" + this.version)
            .addPathSegments("vehicles")
            .addPathSegments(this.vehicleId)
            .addPathSegments(path)
            .build();

    Request request =
        new Request.Builder()
            .url(url)
            .header("Authorization", "Bearer " + accessToken)
            .addHeader("User-Agent", ApiClient.USER_AGENT)
            .method(method, body)
            .header("sc-unit-system", this.unitSystem.name().toLowerCase())
            .build();

    return ApiClient.execute(request, type);
  }

  protected <T extends ApiData> T call(String path, String method, RequestBody body, Class<T> type) throws SmartcarException{
    return this.call(path, method, body, this.accessToken, type);
  }

  protected <T extends ApiData> T call(String path, String method, RequestBody body, Map<String, String> query, Class<T> type)
  throws SmartcarException {
    HttpUrl.Builder urlBuilder =
            HttpUrl.parse(this.origin)
                    .newBuilder()
                    .addPathSegments("v" + this.version)
                    .addPathSegments("vehicles")
                    .addPathSegments(this.vehicleId)
                    .addPathSegments(path);

    for (Map.Entry<String, String> entry: query.entrySet()) {
      urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
    }

    HttpUrl url = urlBuilder.build();

    Request request =
            new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessToken)
                    .addHeader("User-Agent", ApiClient.USER_AGENT)
                    .method(method, body)
                    .header("sc-unit-system", this.unitSystem.name().toLowerCase())
                    .build();

    return ApiClient.execute(request, type);
  }

  /**
   * Send request to the /info endpoint
   *
   * @return VehicleInfo object
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleAttributes attributes() throws SmartcarException {
    return this.call("", "GET", null, VehicleAttributes.class);
  }

  /**
   * Send request to the /vin endpoint
   *
   * @return the vin of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleVin vin() throws SmartcarException {
    return this.call("vin", "GET", null, VehicleVin.class);
  }

  /**
   * Send request to the /permissions endpoint
   *
   * @return the permission of the application
   * @throws SmartcarException if the request is unsuccessful
   */
  public ApplicationPermissions permissions() throws SmartcarException {
    if (this.permissions != null) {
      return this.permissions;
    }

    this.permissions =
          this.call("permissions", "GET", null, ApplicationPermissions.class);
    return this.permissions;
  }

  public ApplicationPermissions permissions(RequestPaging paging) throws SmartcarException {
    if (this.permissions != null) {
      return this.permissions;
    }

    Map<String, String> pagingQuery = new HashMap<String, String>();
    pagingQuery.put("limit", String.valueOf(paging.getLimit()));
    pagingQuery.put("offset", String.valueOf(paging.getOffset()));

    this.permissions = this.call("permissions", "GET", null, pagingQuery, ApplicationPermissions.class);
    return this.permissions;
  }

  /**
   * Send request to the /disconnect endpoint
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public ActionResponse disconnect() throws SmartcarException {
    return this.call("application", "DELETE", null, ActionResponse.class);
  }

  /**
   * Send request to the /odometer endpoint
   *
   * @return the odometer of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleOdometer odometer() throws SmartcarException {
    return this.call("odometer", "GET", null, VehicleOdometer.class);
  }

  /**
   * Send request to the /fuel endpoint
   *
   * @return the fuel status of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleFuel fuel() throws SmartcarException {
    return this.call("fuel", "GET", null, VehicleFuel.class);
  }

  /**
   * Send request to the /engine/oil endpoint
   *
   * @return the engine oil status of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleEngineOil engineOil() throws SmartcarException {
    return this.call("engine/oil", "GET", null, VehicleEngineOil.class);
  }

  /**
   * Send request to the /tires/pressure endpoint
   *
   * @return the tire pressure status of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleTirePressure tirePressure() throws SmartcarException {
    return this.call("tires/pressure", "GET", null, VehicleTirePressure.class);
  }

  /**
   * Send request to the /battery endpoint
   *
   * @return the battery status of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleBattery battery() throws SmartcarException {
    return this.call("battery", "GET", null, VehicleBattery.class);
  }

  /**
   * Send request to the /battery/capacity endpoint
   *
   * @return the battery capacity of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleBatteryCapacity batteryCapacity() throws SmartcarException {
    return this.call("battery/capacity", "GET", null, VehicleBatteryCapacity.class);
  }

  /**
   * Send request to the /charge endpoint
   *
   * @return the charge status of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleCharge charge() throws SmartcarException {
    return this.call("charge", "GET", null, VehicleCharge.class);
  }

  /**
   * Send request to the /location endpoint
   *
   * @return the location of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleLocation location() throws SmartcarException {
    return this.call("location", "GET", null, VehicleLocation.class);
  }

  /**
   * Send request to the /security endpoint to unlock a vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public ActionResponse unlock() throws SmartcarException {
    JsonObject json = Json.createObjectBuilder().add("action", "UNLOCK").build();

    RequestBody body = RequestBody.create(ApiClient.JSON, json.toString());

    return this.call("security", "POST", body, ActionResponse.class);
  }

  /**
   * Send request to the /security endpoint to lock a vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public ActionResponse lock() throws SmartcarException {
    JsonObject json = Json.createObjectBuilder().add("action", "LOCK").build();

    RequestBody body = RequestBody.create(ApiClient.JSON, json.toString());

    return this.call("security", "POST", body, ActionResponse.class);
  }

  /**
   * Send request to the /charge endpoint to start charging a vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public ActionResponse startCharge() throws SmartcarException {
    JsonObject json = Json.createObjectBuilder().add("action", "START").build();

    RequestBody body = RequestBody.create(ApiClient.JSON, json.toString());

    return this.call("charge", "POST", body, ActionResponse.class);
  }

  /**
   * Send request to the /charge endpoint to stop charging a vehicle
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public ActionResponse stopCharge() throws SmartcarException {
    JsonObject json = Json.createObjectBuilder().add("action", "STOP").build();

    RequestBody body = RequestBody.create(ApiClient.JSON, json.toString());

    return this.call("charge", "POST", body, ActionResponse.class);
  }

  /**
   * Subscribe vehicle to a webhook
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public WebhookSubscription subscribe(String webhookId) throws SmartcarException {
    RequestBody body = RequestBody.create(null, new byte[]{});
    return this.call("webhooks/" + webhookId, "POST", body, WebhookSubscription.class);
  }

  /**
   * Unsubscribe vehicle from a webhook
   *
   * @throws SmartcarException if the request is unsuccessful
   */
  public ActionResponse unsubscribe(String applicationManagementToken, String webhookId) throws SmartcarException {
    return this.call("webhooks/" + webhookId, "DELETE", null, applicationManagementToken, ActionResponse.class);
  }

  /**
   * Send request to the /batch endpoint
   *
   * @param paths the paths of endpoints to send requests to (ex. "/odometer", "/location", ...)
   * @return the BatchResponse object containing the response from all the requested endpoints
   * @throws SmartcarException if the request is unsuccessful
   */
  public BatchResponse batch(String[] paths) throws SmartcarException {

    JsonArrayBuilder endpoints = Json.createArrayBuilder();
    for (String path : paths) {
      endpoints.add(Json.createObjectBuilder().add("path", path).build());
    }
    JsonArray requests = endpoints.build();

    JsonObject json = Json.createObjectBuilder().add("requests", requests).build();

    ApiClient.gson.registerTypeAdapter(BatchResponse.class, new BatchDeserializer());
    RequestBody body = RequestBody.create(ApiClient.JSON, json.toString());
    BatchResponse response =
        this.call("batch", "POST", body, BatchResponse.class);
    BatchResponse batchResponse = response;
    batchResponse.setRequestId(response.getMeta().getRequestId());
    return batchResponse;
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
