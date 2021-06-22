package com.smartcar.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smartcar.sdk.data.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.UUID;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.annotations.*;
import okhttp3.mockwebserver.MockResponse;

/** Test Suite: Vehicle */
@PrepareForTest({Vehicle.class, SmartcarException.class})
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
    TestExecutionListener.mockWebServer.takeRequest();
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
            .origin("http://localhost:" + TestExecutionListener.mockWebServer.getPort())
            .build();
    this.subject = new Vehicle(this.vehicleId, this.accessToken, options);
  }

  @Test
  public void testMeta() throws Exception {
    loadAndEnqueueResponse("GetVehicleInfo");

    VehicleOdometer odometer = this.subject.odometer();

    Assert.assertEquals(odometer.getMeta().getRequestId(), this.expectedRequestId);
    Assert.assertEquals(odometer.getMeta().getDataAge().toString(), "Wed Jun 20 01:33:37 PDT 2018");
    Assert.assertEquals(odometer.getMeta().getUnitSystem(), this.unitSystem);
  }

  @Test
  public void testInfo() throws Exception {
    loadAndEnqueueResponse("GetVehicleInfo");

    VehicleInfo info = this.subject.info();

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

    Assert.assertEquals(fuel.getAmountRemaining(), 53.2);
    Assert.assertEquals(fuel.getPercentRemaining(), 0.3);
    Assert.assertEquals(fuel.getRange(), 40.5);
  }

  @Test
  public void testOil() throws Exception {
    loadAndEnqueueResponse("GetEngineOil");

    VehicleOil fuel = this.subject.oil();

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
  }

  @Test
  public void testLock() throws Exception {
    loadAndEnqueueResponse("SecurityAction");

    ActionResponse res = this.subject.lock();

    Assert.assertEquals(res.getStatus(), "success");
  }

  @Test
  public void testStartCharge() throws Exception {
    loadAndEnqueueResponse("ChargingAction");

    ActionResponse res = this.subject.startCharge();

    Assert.assertEquals(res.getStatus(), "success");
  }

  @Test
  public void testStopCharge() throws Exception {
    loadAndEnqueueResponse("ChargingAction");

    ActionResponse res = this.subject.stopCharge();

    Assert.assertEquals(res.getStatus(), "success");
  }

  @Test
  public void testV1PermissionError() throws FileNotFoundException {
    loadAndEnqueueErrorResponse("ErrorPermissionV1", 403);

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      Assert.assertEquals(ex.getStatusCode(), 403);
      Assert.assertEquals(ex.getDescription(), "Insufficient permissions to access requested resource.");
      Assert.assertEquals(ex.getType(), "permission_error");
      Assert.assertNull(ex.getCode());
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
    }

  }

  @Test
  public void testV1VehicleStateError() throws FileNotFoundException {
    loadAndEnqueueErrorResponse("ErrorVehicleStateV1", 409);

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      Assert.assertEquals(ex.getStatusCode(), 409);
      Assert.assertEquals(ex.getDescription(), "Vehicle state cannot be determined.");
      Assert.assertEquals(ex.getType(), "vehicle_state_error");
      Assert.assertEquals(ex.getCode(), "VS_000");
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
    }
  }

  @Test
  public void testV2PermissionError() throws FileNotFoundException {
    loadAndEnqueueErrorResponse("ErrorPermissionV2", 403);

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      Assert.assertEquals(ex.getStatusCode(), 403);
      Assert.assertEquals(ex.getDescription(), "Your application has insufficient permissions to access the requested resource. Please prompt the user to re-authenticate using Smartcar Connect.");
      Assert.assertEquals(ex.getType(), "PERMISSION");
      Assert.assertEquals(ex.getDocURL(), "https://smartcar.com/docs/errors/v2.0/other-errors/#permission");
      Assert.assertEquals(ex.getResolution(), "REAUTHENTICATE");
      Assert.assertNull(ex.getCode());
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
    }
  }

  @Test
  public void testV2VehicleStateError() throws FileNotFoundException  {
    loadAndEnqueueErrorResponse("ErrorVehicleStateV2", 409);

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      Assert.assertEquals(ex.getStatusCode(), 409);
      Assert.assertEquals(ex.getDescription(), "The vehicle is in a sleep state and temporarily unable to perform your request.");
      Assert.assertEquals(ex.getType(), "VEHICLE_STATE");
      Assert.assertEquals(ex.getDocURL(), "https://smartcar.com/docs/errors/v2.0/vehicle-state/#asleep");
      Assert.assertNull(ex.getResolution());
      Assert.assertEquals(ex.getCode(), "ASLEEP");
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
    }
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

    try {
      this.subject.odometer();
    } catch (SmartcarException ex) {
      Assert.assertEquals(ex.getDescription(), "{ \"InvalidJSON\": {");
      Assert.assertEquals(ex.getStatusCode(), 500);
      Assert.assertEquals(ex.getRequestId(), this.expectedRequestId);
      Assert.assertEquals(ex.getType(), "SDK_ERROR");
    }
  }

  @Test
  public void testBatch() throws Exception {
    String expectedRequestId = "67127d3a-a08a-41f0-8211-f96da36b2d6e";
    loadAndEnqueueResponse("BatchResponseSuccess");

    BatchResponse batch = this.subject.batch(new String[] {"/odometer"});
    Assert.assertEquals(batch.getRequestId(), expectedRequestId);
    Assert.assertNull(batch.tirePressure());

    VehicleOdometer odo = batch.odometer();
    Assert.assertEquals(odo.getDistance(), 32768.0);
    Assert.assertEquals(odo.getMeta().getUnitSystem(), "metric");
  }

  @Test
  public void testBatchHTTPError() throws Exception {
    loadAndEnqueueResponse("BatchResponseError");

    BatchResponse batch = this.subject.batch(new String[] {"/odometer"});

    try {
      batch.odometer();
    } catch (SmartcarException e) {
      Assert.assertEquals(e.getStatusCode(), 409);
      Assert.assertEquals(e.getDescription(), "Vehicle state cannot be determined.");
      Assert.assertEquals(e.getMessage(), "vehicle_state_error:VS_000 - Vehicle state cannot be determined.");
      Assert.assertEquals(e.getType(), "vehicle_state_error");
      Assert.assertEquals(e.getCode(), "VS_000");
      Assert.assertEquals(e.getRequestId(), expectedRequestId);
    }
  }

  @Test
  public void testBatchHTTPErrorV2() throws Exception {
    loadAndEnqueueResponse("BatchResponseErrorV2");

    BatchResponse batch = this.subject.batch(new String[] {"/odometer"});

    try {
      batch.odometer();
    } catch (SmartcarException e) {
      Assert.assertEquals(e.getStatusCode(), 409);
      Assert.assertEquals(e.getDescription(), "Door is open.");
      Assert.assertEquals(e.getType(), "VEHICLE_STATE");
      Assert.assertEquals(e.getCode(), "DOOR_OPEN");
      Assert.assertEquals(e.getRequestId(), expectedRequestId);
      Assert.assertEquals(e.getDocURL(), "https://smartcar.com/docs/errors/v2.0/vehicle-state/#door_open");
      Assert.assertNull(e.getResolution());
      Assert.assertNull(e.getDetail());
      Assert.assertEquals(e.getMessage(), "VEHICLE_STATE:DOOR_OPEN - Door is open.");
    }
  }

  @Test
  public void testBatchHTTPErrorV2WithDetail() throws Exception {
    loadAndEnqueueResponse("BatchResponseErrorV2WithDetail");

    BatchResponse batch = this.subject.batch(new String[] {"/odometer"});

    try {
      batch.odometer();
    } catch (SmartcarException e) {
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
      Assert.assertNull(e.getResolution());
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
    }
  }
}
