package com.smartcar.sdk;

import com.google.gson.*;
import com.smartcar.sdk.data.*;
import okhttp3.mockwebserver.MockResponse;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/** Test Suite: Vehicle */
@PowerMockIgnore("javax.net.ssl.*")
public class VehicleTest {

  private final String vehicleId = "902da0a6-796b-4b7e-b092-639677ed1033";
  private final String accessToken = UUID.randomUUID().toString();
  private final String expectedRequestId = "67127d3a-a08a-41f0-8211-f96da36b2d6e";
  private final String dataAge = "2018-06-20T01:33:37.078Z";
  private final String unitSystem = "imperial";

  private Vehicle subject;

  @AfterMethod
  public void afterMethod() throws InterruptedException {
    TestExecutionListener.mockWebServer.takeRequest(1, TimeUnit.SECONDS);
  }

  private JsonElement loadJsonResource(String resourceName) throws FileNotFoundException {
    String fileName = String.format("src/test/resources/%s.json", resourceName);
    return JsonParser.parseReader(new FileReader(fileName));
  }

  private void loadAndEnqueueErrorResponse(String resource, int statusCode) throws FileNotFoundException {
    JsonElement error = loadJsonResource(resource);
    MockResponse mockResponse = new MockResponse()
            .setResponseCode(statusCode)
            .setBody(error.toString())
            .addHeader("sc-request-id", this.expectedRequestId)
            .addHeader("content-type", "application/json");
    TestExecutionListener.mockWebServer.enqueue(mockResponse);
  }

  private void loadAndEnqueueRateLimitErrorResponse(int retryAfter) throws FileNotFoundException {
    JsonElement error = loadJsonResource("ErrorVehicleRateLimit");
    MockResponse mockResponse = new MockResponse()
            .setResponseCode(429)
            .setBody(error.toString())
            .addHeader("sc-request-id", this.expectedRequestId)
            .addHeader("content-type", "application/json")
            .addHeader("retry-after", retryAfter);
    TestExecutionListener.mockWebServer.enqueue(mockResponse);
  }

  private void loadAndEnqueueResponse(String resourceName) throws FileNotFoundException {
    JsonElement success = loadJsonResource(resourceName);
    MockResponse mockResponse = new MockResponse()
            .setBody(success.toString())
            .addHeader("sc-request-id", this.expectedRequestId)
            .addHeader("sc-data-age", this.dataAge)
            .addHeader("sc-unit-system", this.unitSystem);
    TestExecutionListener.mockWebServer.enqueue(mockResponse);
  }

  @BeforeMethod
  private void beforeMethod() throws IOException {

    SmartcarVehicleOptions options = new SmartcarVehicleOptions.Builder()
            .addFlag("foo", "bar")
            .addFlag("test", true)
            .origin("http://localhost:" + TestExecutionListener.mockWebServer.getPort())
            .build();
    this.subject = new Vehicle(this.vehicleId, this.accessToken, options);
  }

  @Test
  public void testMeta() throws Exception {
    loadAndEnqueueResponse("GetVehicleInfo");

    VehicleOdometer odometer = this.subject.odometer();

    Assert.assertEquals(odometer.getMeta().getRequestId(), this.expectedRequestId);
    Assert.assertTrue(odometer.getMeta().getDataAge() instanceof Date);
    Assert.assertEquals(odometer.getMeta().getUnitSystem(), this.unitSystem);
  }

  @Test
  public void testMetaNull() throws SmartcarException {
    MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody("{ 'distance': 100 }")
            .addHeader("sc-request-id", this.expectedRequestId)
            .addHeader("sc-unit-system", this.unitSystem);
    TestExecutionListener.mockWebServer.enqueue(mockResponse);

    VehicleOdometer odo = this.subject.odometer();

    Assert.assertEquals(odo.getMeta().getDataAge(), null);
  }

  @Test
  public void testGetVersion() {
    Assert.assertEquals(this.subject.getVersion(), "2.0");
  }

  @Test
  public void testFlags() {
    Assert.assertEquals(this.subject.getFlags(), "foo:bar test:true");
  }

  @Test
  public void testInfo() throws Exception {
    loadAndEnqueueResponse("GetVehicleInfo");

    VehicleAttributes info = this.subject.attributes();

    Assert.assertEquals(info.getMake(), "TESLA");
    Assert.assertEquals(info.getModel(), "Model S");
    Assert.assertEquals(info.getYear(), 2014);
    Assert.assertEquals(info.getId(), "36ab27d0-fd9d-4455-823a-ce30af709ffc");
    Assert.assertEquals(info.getMeta().getRequestId(), this.expectedRequestId);
  }

  @Test
  public void testVin() throws Exception {
    loadAndEnqueueResponse("GetVehicleVin");

    VehicleVin vin = this.subject.vin();
    Assert.assertEquals(vin.getVin(), "1234A67Q90F2T4567");
  }

  @Test
  public void testPermission() throws Exception {
    loadAndEnqueueResponse("GetPermissions");

    ApplicationPermissions permissions = this.subject.permissions();

    Assert.assertEquals(permissions.getPermissions()[0], "read_vehicle_info");
    Assert.assertEquals(permissions.getMeta().getRequestId(), this.expectedRequestId);
  }

  @Test
  public void testDisconnect() throws Exception {
    loadAndEnqueueResponse("DisconnectVehicle");
    this.subject.disconnect();
  }

  @Test
  public void testOdometer() throws Exception {
    loadAndEnqueueResponse("GetOdometer");

    VehicleOdometer odometer = this.subject.odometer();

    Assert.assertEquals(odometer.getDistance(), 104.32);
  }

  @Test
  public void testFuel() throws Exception {
    loadAndEnqueueResponse("GetFuel");

    VehicleFuel fuel = this.subject.fuel();

    Assert.assertEquals(fuel.getAmountRemaining(), Double.valueOf(53.2));
    Assert.assertEquals(fuel.getPercentRemaining(), Double.valueOf(0.3));
    Assert.assertEquals(fuel.getRange(), Double.valueOf(40.5));
  }

  @Test
  public void testEngineOil() throws Exception {
    loadAndEnqueueResponse("GetEngineOil");

    VehicleEngineOil fuel = this.subject.engineOil();

    Assert.assertEquals(fuel.getLifeRemaining(), 0.35);
  }

  @Test
  public void testTirePressure() throws Exception {
    loadAndEnqueueResponse("GetTirePressure");

    VehicleTirePressure tirePressure = this.subject.tirePressure();

    Assert.assertEquals(tirePressure.getBackLeft(), 219.3);
    Assert.assertEquals(tirePressure.getBackRight(), 219.2);
    Assert.assertEquals(tirePressure.getFrontLeft(), 219.1);
    Assert.assertEquals(tirePressure.getFrontRight(), 219.0);
  }

  @Test
  public void testBattery() throws Exception {
    loadAndEnqueueResponse("GetBatteryLevel");

    VehicleBattery battery = this.subject.battery();

    Assert.assertEquals(battery.getPercentRemaining(), 0.3);
    Assert.assertEquals(battery.getRange(), 40.5);
  }

  @Test
  public void testBatteryCapacity() throws Exception {
    loadAndEnqueueResponse("GetBatteryCapacity");

    VehicleBatteryCapacity batteryCapacity = this.subject.batteryCapacity();

    Assert.assertEquals(batteryCapacity.getCapacity(), 28.0);
  }

  @Test
  public void testGetChargeLimit() throws Exception {
    loadAndEnqueueResponse("GetChargeLimit");

    VehicleChargeLimit chargeLimit = this.subject.getChargeLimit();

    Assert.assertTrue(Double.valueOf(chargeLimit.getChargeLimit()) instanceof Double);
  }

  @Test
  public void testSetChargeLimit() throws Exception {
    loadAndEnqueueResponse("SetChargeLimitAction");

    ActionResponse res = this.subject.setChargeLimit(0.7);

    Assert.assertEquals(res.getStatus(), "success");
  }

  @Test
  public void testCharge() throws Exception {
    loadAndEnqueueResponse("GetChargingStatus");

    VehicleCharge charge = this.subject.charge();

    Assert.assertEquals(charge.getIsPluggedIn(), true);
    Assert.assertEquals(charge.getState(), "FULLY_CHARGED");
  }

  @Test
  public void testLocation() throws Exception {
    loadAndEnqueueResponse("GetLocation");

    VehicleLocation location = this.subject.location();

    Assert.assertEquals(location.getLatitude(), 37.4292);
    Assert.assertEquals(location.getLongitude(), 122.1381);
  }

  @Test
  public void testUnlock() throws Exception {
    loadAndEnqueueResponse("SecurityAction");

    ActionResponse res = this.subject.unlock();

    Assert.assertEquals(res.getStatus(), "success");
    Assert.assertEquals(res.getMessage(), "Message sent successfully to vehicle");
  }

  @Test
  public void testLock() throws Exception {
    loadAndEnqueueResponse("SecurityAction");

    ActionResponse res = this.subject.lock();

    Assert.assertEquals(res.getStatus(), "success");
    Assert.assertEquals(res.getMessage(), "Message sent successfully to vehicle");
  }

  @Test
  public void testStartCharge() throws Exception {
    loadAndEnqueueResponse("ChargingAction");

    ActionResponse res = this.subject.startCharge();

    Assert.assertEquals(res.getStatus(), "success");
    Assert.assertEquals(res.getMessage(), "Message sent successfully to vehicle");
  }

  @Test
  public void testStopCharge() throws Exception {
    loadAndEnqueueResponse("ChargingAction");

    ActionResponse res = this.subject.stopCharge();

    Assert.assertEquals(res.getStatus(), "success");
    Assert.assertEquals(res.getMessage(), "Message sent successfully to vehicle");
  }

  @Test
  public void testVehicleLockStatus() throws Exception {
    loadAndEnqueueResponse("GetVehicleLockStatus");

    VehicleLockStatus lockStatus = this.subject.lockStatus();

    // Lock status
    Assert.assertTrue(lockStatus.isLocked());

    // Doors
    VehicleDoor frontLeftDoor = lockStatus.getDoors()[0];
    Assert.assertEquals(frontLeftDoor.getType(), "frontLeft");
    Assert.assertEquals(frontLeftDoor.getStatus(), "LOCKED");

    VehicleDoor frontRightDoor = lockStatus.getDoors()[1];
    Assert.assertEquals(frontRightDoor.getType(), "frontRight");
    Assert.assertEquals(frontRightDoor.getStatus(), "LOCKED");

    VehicleDoor backLeftDoor = lockStatus.getDoors()[2];
    Assert.assertEquals(backLeftDoor.getType(), "backLeft");
    Assert.assertEquals(backLeftDoor.getStatus(), "LOCKED");

    VehicleDoor backRightDoor = lockStatus.getDoors()[3];
    Assert.assertEquals(backRightDoor.getType(), "backRight");
    Assert.assertEquals(backRightDoor.getStatus(), "LOCKED");

    // Windows
    VehicleWindow frontLeftWindow = lockStatus.getWindows()[0];
    Assert.assertEquals(frontLeftWindow.getType(), "frontLeft");
    Assert.assertEquals(frontLeftWindow.getStatus(), "CLOSED");

    VehicleWindow frontRightWindow = lockStatus.getWindows()[1];
    Assert.assertEquals(frontRightWindow.getType(), "frontRight");
    Assert.assertEquals(frontRightWindow.getStatus(), "CLOSED");

    VehicleWindow backLeftWindow = lockStatus.getWindows()[2];
    Assert.assertEquals(backLeftWindow.getType(), "backLeft");
    Assert.assertEquals(backLeftWindow.getStatus(), "CLOSED");

    VehicleWindow backRightWindow = lockStatus.getWindows()[3];
    Assert.assertEquals(backRightWindow.getType(), "backRight");
    Assert.assertEquals(backRightWindow.getStatus(), "CLOSED");

    // Sunroof
    VehicleSunroof sunroof = lockStatus.getSunroof()[0];
    Assert.assertEquals(sunroof.getType(), "sunroof");
    Assert.assertEquals(sunroof.getStatus(), "CLOSED");

    // Storage
    VehicleStorage rearStorage = lockStatus.getStorage()[0];
    Assert.assertEquals(rearStorage.getType(), "rear");
    Assert.assertEquals(rearStorage.getStatus(), "CLOSED");

    VehicleStorage frontStorage = lockStatus.getStorage()[1];
    Assert.assertEquals(frontStorage.getType(), "front");
    Assert.assertEquals(frontStorage.getStatus(), "CLOSED");

    // Charging port
    VehicleChargingPort chargingPort = lockStatus.getChargingPort()[0];
    Assert.assertEquals(chargingPort.getType(), "chargingPort");
    Assert.assertEquals(chargingPort.getStatus(), "CLOSED");
  }

  @Test
  public void testSendDestination() throws Exception {
    loadAndEnqueueResponse("SendDestination");

    ActionResponse res = this.subject.sendDestination(47.6205063, -122.3518523);

    Assert.assertEquals(res.getStatus(), "success");
    Assert.assertEquals(res.getMessage(), "Message sent successfully to vehicle");
  }

  @Test
  public void testSendDestinationIllegalArgumentLatitude() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> this.subject.sendDestination(147.6205063, -122.3518523));
  }

  @Test
  public void testSendDestinationIllegalArgumentLongitude() {
    Assert.assertThrows(IllegalArgumentException.class,
            () -> this.subject.sendDestination(47.6205063, -192.3518523));
  }

  @Test
  public void testSubscribe() throws Exception {
    loadAndEnqueueResponse("SubscribeVehicle");

    WebhookSubscription res = this.subject.subscribe("sampleId");

    Assert.assertEquals(res.getVehicleId(), "902da0a6-796b-4b7e-b092-639677ed1033");
    Assert.assertEquals(res.getWebhookId(), "d0846f13-ef13-4014-9d90-6439e1dd0884");
  }

  @Test
  public void testUnsubscribe() throws Exception {
    loadAndEnqueueResponse("UnsubscribeVehicle");

    this.subject.unsubscribe("token", "sampleId");
  }

  @Test
  public void testRequestOdometer() throws Exception {
    loadAndEnqueueResponse("GetOdometer");

    SmartcarVehicleRequest request = new SmartcarVehicleRequest.Builder()
            .method("GET")
            .path("odometer")
            .addHeader("sc-unit-system", "imperial")
            .addFlag("foo", "bar")
            .build();

    VehicleResponse odometer = this.subject.request(request);

    Assert.assertEquals(odometer.getBodyAsString(), "{\"distance\":104.32}");
    Assert.assertEquals(odometer.getBody().get("distance"), new JsonPrimitive(104.32));
    Assert.assertEquals(odometer.getMeta().getRequestId(), "67127d3a-a08a-41f0-8211-f96da36b2d6e");
    Assert.assertEquals(odometer.getMeta().getUnitSystem(), "imperial");
  }

  @Test
  public void testRequestBatch() throws Exception {
    loadAndEnqueueResponse("BatchResponseSuccess");

    String[] paths = new String[]{"/odometer", "/tires/pressure"};
    JsonArrayBuilder endpoints = Json.createArrayBuilder();
    for (String path : paths) {
      endpoints.add(Json.createObjectBuilder().add("path", path));
    }
    javax.json.JsonArray requests = endpoints.build();

    SmartcarVehicleRequest request = new SmartcarVehicleRequest.Builder()
            .method("POST")
            .path("batch")
            .addBodyParameter("requests", requests)
            .addHeader("sc-unit-system", "imperial")
            .build();

    VehicleResponse batchResponse = this.subject.request(request);
    Assert.assertEquals(batchResponse.getMeta().getRequestId(), "67127d3a-a08a-41f0-8211-f96da36b2d6e");

    JsonArray responsesArray = batchResponse.getBody().get("responses").getAsJsonArray();

    BatchResponse response = new BatchResponse(responsesArray);

    Assert.assertEquals(responsesArray.size(), 1);

    VehicleOdometer odometer = response.odometer();

    Assert.assertEquals(odometer.getDistance(), 32768.0);
    Assert.assertEquals(odometer.getMeta().getUnitSystem(), "metric");
  }

  @Test
  public void testLockStatusBatch() throws Exception {
    loadAndEnqueueResponse("BatchLockStatusResponseSuccess");

    String[] paths = new String[]{"/security"};
    JsonArrayBuilder endpoints = Json.createArrayBuilder();
    for (String path : paths) {
      endpoints.add(Json.createObjectBuilder().add("path", path));
    }
    javax.json.JsonArray requests = endpoints.build();

    SmartcarVehicleRequest request = new SmartcarVehicleRequest.Builder()
            .method("POST")
            .path("batch")
            .addBodyParameter("requests", requests)
            .build();

    VehicleResponse batchResponse = this.subject.request(request);
    Assert.assertEquals(batchResponse.getMeta().getRequestId(), "67127d3a-a08a-41f0-8211-f96da36b2d6e");

    JsonArray responsesArray = batchResponse.getBody().get("responses").getAsJsonArray();

    BatchResponse response = new BatchResponse(responsesArray);

    Assert.assertEquals(responsesArray.size(), 1);

    VehicleLockStatus lockStatus = response.lockStatus();

    Assert.assertTrue(lockStatus.isLocked());
    Assert.assertEquals(lockStatus.getDoors().length, 4);
    Assert.assertEquals(lockStatus.getWindows().length, 4);
    Assert.assertEquals(lockStatus.getSunroof().length, 1);
    Assert.assertEquals(lockStatus.getStorage().length, 2);
    Assert.assertEquals(lockStatus.getChargingPort().length, 1);
  }

  @Test
  public void testV1PermissionError() throws FileNotFoundException {
    loadAndEnqueueErrorResponse("ErrorPermissionV1", 403);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getStatusCode(), 403);
      Assert.assertEquals(ex.getDescription(), "Insufficient permissions to access requested resource.");
      Assert.assertEquals(ex.getType(), "permission_error");
      Assert.assertNull(ex.getCode());
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testV1VehicleStateError() throws FileNotFoundException {
    loadAndEnqueueErrorResponse("ErrorVehicleStateV1", 409);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getStatusCode(), 409);
      Assert.assertEquals(ex.getDescription(), "Vehicle state cannot be determined.");
      Assert.assertEquals(ex.getType(), "vehicle_state_error");
      Assert.assertEquals(ex.getCode(), "VS_000");
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testV2PermissionError() throws FileNotFoundException {
    loadAndEnqueueErrorResponse("ErrorPermissionV2", 403);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getStatusCode(), 403);
      Assert.assertEquals(ex.getDescription(), "Your application has insufficient permissions to access the requested resource. Please prompt the user to re-authenticate using Smartcar Connect.");
      Assert.assertEquals(ex.getType(), "PERMISSION");
      Assert.assertEquals(ex.getDocURL(), "https://smartcar.com/docs/errors/v2.0/other-errors/#permission");
      Assert.assertEquals(ex.getResolutionType(), "REAUTHENTICATE");
      Assert.assertNull(ex.getResolutionUrl());
      Assert.assertNull(ex.getCode());
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
    }
    Assert.assertTrue(thrown);
  }

  @Test
  public void testV2VehicleStateError() throws FileNotFoundException  {
    loadAndEnqueueErrorResponse("ErrorVehicleStateV2", 409);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getStatusCode(), 409);
      Assert.assertEquals(ex.getDescription(), "The vehicle is in a sleep state and temporarily unable to perform your request.");
      Assert.assertEquals(ex.getType(), "VEHICLE_STATE");
      Assert.assertEquals(ex.getDocURL(), "https://smartcar.com/docs/errors/v2.0/vehicle-state/#asleep");
      Assert.assertNull(ex.getResolutionType());
      Assert.assertEquals(ex.getCode(), "ASLEEP");
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
    }

    Assert.assertTrue(thrown);
  }
  @Test
  public void testV2RateLimitError() throws FileNotFoundException  {
    loadAndEnqueueRateLimitErrorResponse(12345);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getStatusCode(), 429);
      Assert.assertEquals(ex.getRetryAfter(), 12345);
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testInvalidJsonResponse() {
    MockResponse mockResponse = new MockResponse()
            .setResponseCode(500)
            .setBody("{ \"InvalidJSON\": {")
            .addHeader("sc-request-id", this.expectedRequestId)
            .addHeader("sc-data-age", this.dataAge)
            .addHeader("sc-unit-system", this.unitSystem);
    TestExecutionListener.mockWebServer.enqueue(mockResponse);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getDescription(), "{ \"InvalidJSON\": {");
      Assert.assertEquals(ex.getStatusCode(), 500);
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
      Assert.assertEquals(ex.getType(), "SDK_ERROR");
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testNullErrorResponse() {
    MockResponse mockResponse = new MockResponse()
            .setResponseCode(500);
    TestExecutionListener.mockWebServer.enqueue(mockResponse);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getDescription(), "Empty response body");
      Assert.assertEquals(ex.getStatusCode(), 500);
      Assert.assertEquals(ex.getType(), "SDK_ERROR");
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testNull200Response() {
    MockResponse mockResponse = new MockResponse()
            .setResponseCode(200);
    TestExecutionListener.mockWebServer.enqueue(mockResponse);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getDescription(), "Empty response body");
      Assert.assertEquals(ex.getStatusCode(), 200);
      Assert.assertEquals(ex.getType(), "SDK_ERROR");
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testErrorResolutionObject() throws Exception {
    loadAndEnqueueErrorResponse("ErrorResolutionObject", 401);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getResolutionType(), "REAUTHENTICATE");
      Assert.assertEquals(ex.getResolutionUrl(), "https://example.com");
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testErrorResolutionObjectNoUrl() throws Exception {
    loadAndEnqueueErrorResponse("ErrorResolutionObject2", 401);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertEquals(ex.getResolutionType(), "REAUTHENTICATE");
      Assert.assertNull(ex.getResolutionUrl());
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testErrorResolutionObjectNullType() throws Exception {
    loadAndEnqueueErrorResponse("ErrorResolutionObject3", 401);
    boolean thrown = false;

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      thrown = true;
      Assert.assertNull(ex.getResolutionType());
      Assert.assertNull(ex.getResolutionUrl());
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testBatch() throws Exception {
    String expectedRequestId = "67127d3a-a08a-41f0-8211-f96da36b2d6e";
    loadAndEnqueueResponse("BatchResponseSuccess");

    BatchResponse batch = this.subject.batch(new String[] {"/odometer"});
    Assert.assertEquals(batch.getRequestId(), expectedRequestId);
    boolean thrown = false;
    try {
      batch.tirePressure();
    } catch (SmartcarException e) {
      thrown = true;
      Assert.assertEquals(e.getType(), "DATA_NOT_FOUND");
      Assert.assertEquals(e.getDescription(), "The data you requested was not returned");
    }
    Assert.assertTrue(thrown);

    VehicleOdometer odo = batch.odometer();
    Assert.assertEquals(odo.getDistance(), 32768.0);
    Assert.assertEquals(odo.getMeta().getUnitSystem(), "metric");
    Assert.assertEquals(odo.getMeta().getRequestId(), "67127d3a-a08a-41f0-8211-f96da36b2d6e");
  }

  @Test
  public void testBatchHTTPError() throws Exception {
    loadAndEnqueueResponse("BatchResponseError");

    BatchResponse batch = this.subject.batch(new String[] {"/odometer"});
    boolean thrown = false;

    try {
      batch.odometer();
    } catch (SmartcarException e) {
      thrown = true;
      Assert.assertEquals(e.getStatusCode(), 409);
      Assert.assertEquals(e.getDescription(), "Vehicle state cannot be determined.");
      Assert.assertEquals(e.getMessage(), "vehicle_state_error:VS_000 - Vehicle state cannot be determined.");
      Assert.assertEquals(e.getType(), "vehicle_state_error");
      Assert.assertEquals(e.getCode(), "VS_000");
      Assert.assertEquals(e.getRequestId(), expectedRequestId);
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testBatchHTTPErrorV2() throws Exception {
    loadAndEnqueueResponse("BatchResponseErrorV2");

    BatchResponse batch = this.subject.batch(new String[] {"/odometer"});
    boolean thrown = false;

    try {
      batch.odometer();
    } catch (SmartcarException e) {
      thrown = true;
      Assert.assertEquals(e.getStatusCode(), 409);
      Assert.assertEquals(e.getDescription(), "Door is open.");
      Assert.assertEquals(e.getType(), "VEHICLE_STATE");
      Assert.assertEquals(e.getCode(), "DOOR_OPEN");
      Assert.assertEquals(e.getRequestId(), expectedRequestId);
      Assert.assertEquals(e.getDocURL(), "https://smartcar.com/docs/errors/v2.0/vehicle-state/#door_open");
      Assert.assertNull(e.getResolutionType());
      Assert.assertNull(e.getDetail());
      Assert.assertEquals(e.getMessage(), "VEHICLE_STATE:DOOR_OPEN - Door is open.");
      Assert.assertEquals(e.getRetryAfter(), 0);
    }
    Assert.assertTrue(thrown);
  }

  @Test
  public void testBatchHTTPErrorV2WithDetail() throws Exception {
    loadAndEnqueueResponse("BatchResponseErrorV2WithDetail");

    BatchResponse batch = this.subject.batch(new String[] {"/odometer"});
    boolean thrown = false;

    try {
      batch.odometer();
    } catch (SmartcarException e) {
      thrown = true;
      Assert.assertEquals(e.getStatusCode(), 400);
      Assert.assertEquals(
          e.getDescription(),
          "Request invalid or malformed. Please check for missing parameters, spelling and casing"
              + " mistakes, and other syntax issues.");
      Assert.assertEquals(e.getType(), "VALIDATION");
      Assert.assertNull(e.getCode());
      Assert.assertEquals(e.getRequestId(), expectedRequestId);
      Assert.assertEquals(
          e.getDocURL(), "https://smartcar.com/docs/errors/v2.0/other-errors/#validation");
      Assert.assertNull(e.getResolutionType());
      Assert.assertEquals(
          e.getDetail().get(0).toString(),
          "{\"field\":[\"requests\"],\"message\":\"\\\"requests\\\" is required\"}");
      Assert.assertEquals(
          e.getDetail().get(1).toString(),
          "{\"field\":[\"request\"],\"message\":\"\\\"request\\\" is not allowed\"}");
      Assert.assertEquals(
          e.getMessage(),
          "VALIDATION:null - Request invalid or malformed. Please check for missing parameters,"
              + " spelling and casing mistakes, and other syntax issues.");
      Assert.assertEquals(e.getRetryAfter(), 0);
    }

    Assert.assertTrue(thrown);
  }

  @Test
  public void testBatchMixedErrorsSuccess() throws FileNotFoundException, SmartcarException {
    loadAndEnqueueResponse("BatchResponseMixed");
    boolean thrown = false;

    BatchResponse batch = this.subject.batch(new String[] {"/odometer", "/fuel"});
    VehicleOdometer odo = batch.odometer();
    try {
      batch.fuel();
    } catch (SmartcarException e) {
      thrown = true;
      Assert.assertEquals(e.getStatusCode(), 409);
    }

    Assert.assertTrue(thrown);
  }
}
