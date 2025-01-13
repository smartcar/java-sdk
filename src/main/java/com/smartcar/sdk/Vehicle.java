package com.smartcar.sdk;

import com.smartcar.sdk.data.*;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/** Smartcar Vehicle API Object */
public class Vehicle {
  public enum UnitSystem {
    IMPERIAL,
    METRIC,
  }

  private static final double LATITUDE_MIN = -90.0;
  private static final double LATITUDE_MAX = 90.0;
  private static final double LONGITUDE_MIN = -180.0;
  private static final double LONGITUDE_MAX = 180.0;

  private final String vehicleId;
  private final String accessToken;
  private Vehicle.UnitSystem unitSystem;
  private final String version;
  private final String origin;
  private final String flags;
  private ApplicationPermissions permissions;

  /**
   * Initializes a new Vehicle.
   *
   * @param vehicleId   the vehicle ID
   * @param accessToken the OAuth 2.0 access token
   */
  public Vehicle(String vehicleId, String accessToken) {
    this(vehicleId, accessToken, new SmartcarVehicleOptions.Builder().build());
  }

  /**
   * Initializes a new Vehicle with provided options
   * 
   * @param vehicleId   vehicleId the vehicle ID
   * @param accessToken accessToken the OAuth 2.0 access token
   * @param options     optional arguments provided with a SmartcarVehicleOptions
   *                    instance
   */
  public Vehicle(String vehicleId, String accessToken, SmartcarVehicleOptions options) {
    this.vehicleId = vehicleId;
    this.accessToken = accessToken;
    this.version = options.getVersion();
    this.unitSystem = options.getUnitSystem();
    this.origin = options.getOrigin();
    this.flags = options.getFlags();
  }

  /**
   * Gets the version of Smartcar API that this vehicle is using
   * 
   * @return String representing version
   */
  public String getVersion() {
    return this.version;
  }

  /**
   * Gets the flags that are passed to the vehicle object as a serialized string
   * 
   * @return serialized string of the flags
   */
  public String getFlags() {
    return this.flags;
  }

  /**
   * Executes an API request under the VehicleIds endpoint.
   *
   * @param path   the path to the sub-endpoint
   * @param method the method of the request
   * @param body   the body of the request
   * @param type   the type into which the response will be parsed
   * @return the parsed response
   * @throws SmartcarException if the request is unsuccessful
   */
  protected <T extends ApiData> T call(
      String path, String method, RequestBody body, String accessToken, Class<T> type) throws SmartcarException {
    HttpUrl.Builder urlBuilder = HttpUrl.parse(this.origin)
        .newBuilder()
        .addPathSegments("v" + this.version)
        .addPathSegments("vehicles")
        .addPathSegments(this.vehicleId)
        .addPathSegments(path);

    if (this.getFlags() != null) {
      urlBuilder.addQueryParameter("flags", this.getFlags());
    }
    HttpUrl url = urlBuilder.build();

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + accessToken);
    headers.put("sc-unit-system", this.unitSystem.name().toLowerCase());
    Request request = ApiClient.buildRequest(url, method, body, headers);

    return ApiClient.execute(request, type);
  }

  protected <T extends ApiData> T call(String path, String method, RequestBody body, Class<T> type)
      throws SmartcarException {
    return this.call(path, method, body, this.accessToken, type);
  }

  protected <T extends ApiData> T call(String path, String method, RequestBody body, Map<String, String> query,
      Class<T> type)
      throws SmartcarException {
    HttpUrl.Builder urlBuilder = HttpUrl.parse(this.origin)
        .newBuilder()
        .addPathSegments("v" + this.version)
        .addPathSegments("vehicles")
        .addPathSegments(this.vehicleId)
        .addPathSegments(path);

    for (Map.Entry<String, String> entry : query.entrySet()) {
      urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
    }
    if (this.getFlags() != null) {
      urlBuilder.addQueryParameter("flags", this.getFlags());
    }
    HttpUrl url = urlBuilder.build();

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + accessToken);
    headers.put("sc-unit-system", this.unitSystem.name().toLowerCase());
    Request request = ApiClient.buildRequest(url, method, body, headers);

    return ApiClient.execute(request, type);
  }

  /**
   * Send request to the / endpoint
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

    this.permissions = this.call("permissions", "GET", null, ApplicationPermissions.class);
    return this.permissions;
  }

  /**
   * Send request to the /permissions endpoint with paging options set
   *
   * @param paging Request paging options
   * @return the permissons of the application
   * @throws SmartcarException if the request is unsuccessful
   */
  public ApplicationPermissions permissions(RequestPaging paging) throws SmartcarException {
    Map<String, String> pagingQuery = new HashMap<String, String>();
    pagingQuery.put("limit", String.valueOf(paging.getLimit()));
    pagingQuery.put("offset", String.valueOf(paging.getOffset()));

    this.permissions = this.call("permissions", "GET", null, pagingQuery, ApplicationPermissions.class);
    return this.permissions;
  }

  /**
   * Send request to the /disconnect endpoint
   *
   * @return a response indicating success
   * @throws SmartcarException if the request is unsuccessful
   */
  public DisconnectResponse disconnect() throws SmartcarException {
    return this.call("application", "DELETE", null, DisconnectResponse.class);
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
   * Returns a list of all the service records performed on the vehicle,
   * filtered by the optional date range. If no dates are specified, records from
   * the last year are returned.
   *
   * @param startDate the start date of the period to retrieve records from
   *                  (inclusive)
   * @param endDate   the end date of the period to retrieve records until
   *                  (inclusive)
   * @return service history records
   * @throws SmartcarException            if the request is unsuccessful
   * @throws UnsupportedEncodingException
   */
  public ServiceHistory serviceHistory(OffsetDateTime startDate, OffsetDateTime endDate)
      throws SmartcarException, UnsupportedEncodingException {
    if (startDate == null || endDate == null) {
      // If dates are not specified, default to the last year
      endDate = OffsetDateTime.now(ZoneOffset.UTC);
      startDate = endDate.minusYears(1);
    }

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    String formattedStartDate = startDate.format(dateFormatter);
    String formattedEndDate = endDate.format(dateFormatter);

    String encodedStartDate = URLEncoder.encode(formattedStartDate, StandardCharsets.UTF_8.toString());
    String encodedEndDate = URLEncoder.encode(formattedEndDate, StandardCharsets.UTF_8.toString());

    String url = "service/history?start_date=" + encodedStartDate + "&end_date=" + encodedEndDate;

    return this.call(url, "GET", null, ServiceHistory.class);
  }

  /**
   * Overload without parameters to handle no input case, calling the full method
   * with nulls
   * 
   * @return service history records
   * @throws SmartcarException            if the request is unsuccessful
   * @throws UnsupportedEncodingException
   */
  public ServiceHistory serviceHistory() throws SmartcarException, UnsupportedEncodingException {
    return serviceHistory(null, null);
  }

  /**
   * Retrieves the diagnostic system status of the vehicle.
   *
   * @return the diagnostic system status of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleDiagnosticSystemStatus diagnosticSystemStatus() throws SmartcarException {
    return this.call("diagnostics/system_status", "GET", null, VehicleDiagnosticSystemStatus.class);
  }

  /**
   * Retrieves the diagnostic trouble codes of the vehicle.
   *
   * @return the diagnostic trouble codes of the vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleDiagnosticTroubleCodes diagnosticTroubleCodes() throws SmartcarException {
    return this.call("diagnostics/dtcs", "GET", null, VehicleDiagnosticTroubleCodes.class);
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
   * Send request to the /battery/nominal_capacity endpoint
   *
   * @return Returns a list of nominal rated battery capacities for a vehicle
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleNominalCapacity nominalCapacity() throws SmartcarException {
    return this.call("battery/nominal_capacity", "GET", null, VehicleNominalCapacity.class);
  }

  /**
   * Get a vehicle's charge limit
   *
   * @return the charge limit of the vehicle as a float value between 0 and 1.
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleChargeLimit getChargeLimit() throws SmartcarException {
    return this.call("charge/limit", "GET", null, VehicleChargeLimit.class);
  }

  /**
   * Set a vehicle's charge limit
   *
   * @param limit the new charge limit to set.
   * @return a response indicating success
   * @throws SmartcarException if the request is unsuccessful
   */
  public ActionResponse setChargeLimit(double limit) throws SmartcarException {
    JsonObject json = Json.createObjectBuilder().add("limit", limit).build();

    RequestBody body = RequestBody.create(ApiClient.JSON, json.toString());

    return this.call("charge/limit", "POST", body, ActionResponse.class);
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
   * @return a response indicating success
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
   * @return a response indicating success
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
   * @return a response indicating success
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
   * @return a response indicating success
   * @throws SmartcarException if the request is unsuccessful
   */
  public ActionResponse stopCharge() throws SmartcarException {
    JsonObject json = Json.createObjectBuilder().add("action", "STOP").build();

    RequestBody body = RequestBody.create(ApiClient.JSON, json.toString());

    return this.call("charge", "POST", body, ActionResponse.class);
  }

  /**
   * Send request to the /security endpoint to get the vehicle's lock status
   *
   * @return VehicleLockStatus
   * @throws SmartcarException
   */
  public VehicleLockStatus lockStatus() throws SmartcarException {
    return this.call("security", "GET", null, VehicleLockStatus.class);
  }

  /**
   * Send request to the /navigation/destination endpoint to set the navigation
   * destination
   *
   * @param latitude  A double representing the destination's latitude
   * @param longitude A double representing the destination's longitude
   * @return a response indicating success
   * @throws SmartcarException        if the request is unsuccessful
   * @throws IllegalArgumentException if the latitude is not between -90.0 and
   *                                  90.0 or
   *                                  if the longitude is not between -180.0 and
   *                                  180.0
   */
  public ActionResponse sendDestination(double latitude, double longitude) throws SmartcarException {
    if (latitude < LATITUDE_MIN || latitude > LATITUDE_MAX) {
      throw new IllegalArgumentException(
          String.format("Latitude must be between %f and %f", LATITUDE_MIN, LATITUDE_MAX));
    }
    if (longitude < LONGITUDE_MIN || longitude > LONGITUDE_MAX) {
      throw new IllegalArgumentException(
          String.format("Longitude must be between %f and %f", LONGITUDE_MIN, LONGITUDE_MAX));
    }

    JsonObject json = Json.createObjectBuilder()
        .add("latitude", latitude)
        .add("longitude", longitude)
        .build();

    RequestBody requestBody = RequestBody.create(ApiClient.JSON, json.toString());

    return this.call("navigation/destination", "POST", requestBody, ActionResponse.class);
  }

  /**
   * Subscribe vehicle to a webhook
   *
   * @return information about the webhook and vehicle subscription
   * @throws SmartcarException if the request is unsuccessful
   */
  public WebhookSubscription subscribe(String webhookId) throws SmartcarException {
    RequestBody body = RequestBody.create(null, new byte[] {});
    return this.call("webhooks/" + webhookId, "POST", body, WebhookSubscription.class);
  }

  /**
   * Unsubscribe vehicle from a webhook
   *
   * @return response indicating successful removal from the subscription
   * @throws SmartcarException if the request is unsuccessful
   */
  public UnsubscribeResponse unsubscribe(String applicationManagementToken, String webhookId) throws SmartcarException {
    return this.call("webhooks/" + webhookId, "DELETE", null, applicationManagementToken, UnsubscribeResponse.class);
  }

  /**
   * Send request to the /batch endpoint
   *
   * @param paths the paths of endpoints to send requests to (ex. "/odometer",
   *              "/location", ...)
   * @return the BatchResponse object containing the response from all the
   *         requested endpoints
   * @throws SmartcarException if the request is unsuccessful
   */
  public BatchResponse batch(String[] paths) throws SmartcarException {

    JsonArrayBuilder endpoints = Json.createArrayBuilder();
    for (String path : paths) {
      endpoints.add(Json.createObjectBuilder().add("path", path).build());
    }
    JsonArray requests = endpoints.build();

    JsonObject json = Json.createObjectBuilder().add("requests", requests).build();

    RequestBody body = RequestBody.create(ApiClient.JSON, json.toString());
    BatchResponse response = this.call("batch", "POST", body, BatchResponse.class);
    BatchResponse batchResponse = response;
    batchResponse.setRequestId(response.getMeta().getRequestId());
    return batchResponse;
  }

  /**
   * General purpose method to make a request to a Smartcar endpoint - can be used
   * to make requests to brand specific endpoints.
   *
   * @param vehicleRequest with options for this request. See
   *                       Smartcar.SmartcarVehicleRequest
   * @return the VehicleResponse object containing the response from the requested
   *         endpoint
   * @throws SmartcarException if the request is unsuccessful
   */
  public VehicleResponse request(SmartcarVehicleRequest vehicleRequest) throws SmartcarException, IOException {
    HttpUrl.Builder urlBuilder = HttpUrl.parse(this.origin)
        .newBuilder()
        .addPathSegments("v" + this.version)
        .addPathSegments("vehicles")
        .addPathSegments(this.vehicleId)
        .addPathSegments(vehicleRequest.getPath());

    if (this.flags != null) {
      urlBuilder.addQueryParameter("flags", this.flags);
    }

    // This overrides the defaults flags
    if (vehicleRequest.getFlags() != null) {
      urlBuilder.addQueryParameter("flags", vehicleRequest.getFlags());
    }

    vehicleRequest.getQueryParams().forEach(urlBuilder::addQueryParameter);

    HttpUrl url = urlBuilder.build();

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + accessToken);
    headers.put("sc-unit-system", this.unitSystem.name().toLowerCase());

    // Overrides generated headers
    headers.putAll(vehicleRequest.getHeaders());

    Request request = ApiClient.buildRequest(url,
        vehicleRequest.getMethod(),
        vehicleRequest.getBody(),
        headers);

    VehicleResponse vehicleResponse = ApiClient.execute(request, VehicleResponse.class);

    return vehicleResponse;
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
