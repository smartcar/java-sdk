package com.smartcar.sdk;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smartcar.sdk.data.ApiData;
import com.smartcar.sdk.data.ApplicationPermissions;
import com.smartcar.sdk.data.BatchResponse;
import com.smartcar.sdk.data.ResponsePaging;
import com.smartcar.sdk.data.SmartcarResponse;
import com.smartcar.sdk.data.VehicleBattery;
import com.smartcar.sdk.data.VehicleCharge;
import com.smartcar.sdk.data.VehicleFuel;
import com.smartcar.sdk.data.VehicleInfo;
import com.smartcar.sdk.data.VehicleLocation;
import com.smartcar.sdk.data.VehicleOdometer;
import com.smartcar.sdk.data.VehicleOil;
import com.smartcar.sdk.data.VehicleTirePressure;
import com.smartcar.sdk.data.VehicleVin;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import okhttp3.RequestBody;


/**
 * Test Suite: Vehicle
 */
@PrepareForTest({
  Vehicle.class,
  SmartcarException.class
})
public class VehicleTest {

  private final String vehicleId = UUID.randomUUID().toString();
  private final String accessToken = UUID.randomUUID().toString();

  private Vehicle subject;

  @BeforeMethod
  private void beforeMethod() {
    this.subject = PowerMockito.spy(new Vehicle(
      this.vehicleId,
      this.accessToken
    ));
  }

  @Test
  public void testInfo() throws Exception {
    String id = UUID.randomUUID().toString();
    String make = "MOCK";
    String model = "model";
    int year = 2018;
    VehicleInfo expected = new VehicleInfo(id, make, model, year);
    SmartcarResponse res = new SmartcarResponse<>(expected);

    PowerMockito.doReturn(res).when(this.subject).call("", "GET", null, VehicleInfo.class);

    VehicleInfo info = this.subject.info();

    Assert.assertEquals(info, expected);
  }

  @Test
  public void testVin() throws Exception {
    String expectedVin = "2HKRM4H35EH681798";
    VehicleVin expected = new VehicleVin(expectedVin);
    SmartcarResponse res = new SmartcarResponse<>(expected);

    PowerMockito.doReturn(res).when(this.subject).call("vin", "GET", null, VehicleVin.class);
    String vin = this.subject.vin();

    Assert.assertEquals(vin, expectedVin);
  }

  @Test
  public void testPermission() throws Exception {
    String[] expectedPermissions = new String[] {"read_odometer"};
    ResponsePaging paging = new ResponsePaging(10, 0);
    ApplicationPermissions expected = new ApplicationPermissions(paging, expectedPermissions);
    SmartcarResponse res = new SmartcarResponse<>(expected);

    PowerMockito.doReturn(res).when(this.subject).call("permissions", "GET", null, ApplicationPermissions.class);

    String[] permissions = this.subject.permissions();

    Assert.assertEquals(permissions, expectedPermissions);
  }


  @Test
  public void testHasPermission() throws Exception {
    String[] expectedPermissions = new String[] {"read_odometer", "read_vehicle_info", "read_location"};
    ResponsePaging paging = new ResponsePaging(10, 0);
    ApplicationPermissions expected = new ApplicationPermissions(paging, expectedPermissions);
    SmartcarResponse res = new SmartcarResponse<>(expected);

    PowerMockito.doReturn(res).when(this.subject).call("permissions", "GET", null, ApplicationPermissions.class);

    Assert.assertTrue(this.subject.hasPermissions("required:read_odometer"));
    Assert.assertTrue(this.subject.hasPermissions(new String[] {"read_odometer", "required:read_vehicle_info"}));
  }

  @Test
  public void testDoesNotHavePermission() throws Exception {
    String[] expectedPermissions = new String[] {"read_odometer"};
    ResponsePaging paging = new ResponsePaging(10, 0);
    ApplicationPermissions expected = new ApplicationPermissions(paging, expectedPermissions);
    SmartcarResponse res = new SmartcarResponse<>(expected);

    PowerMockito.doReturn(res).when(this.subject).call("permissions", "GET", null, ApplicationPermissions.class);

    Assert.assertFalse(this.subject.hasPermissions("read_vehicle_info"));
    Assert.assertFalse(this.subject.hasPermissions(new String[] {"read_odometer", "read_vehicle_info"}));
  }

  @Test
  public void testDisconnect() throws Exception {

    ApiData data = new ApiData();
    SmartcarResponse res = new SmartcarResponse(data);

    PowerMockito.doReturn(res.toString()).when(this.subject).call("application", "DELETE", null);

    this.subject.disconnect();
  }

  @Test
  public void testOdometer() throws Exception {
    int expectedDistance = 1234;
    VehicleOdometer expected = new VehicleOdometer(expectedDistance);
    String unitSystem = "metric";
    Date age = new Date();
    SmartcarResponse res = new SmartcarResponse<>(expected, unitSystem, age);

    PowerMockito.doReturn(res).when(this.subject).call("odometer", "GET", null, VehicleOdometer.class);

    SmartcarResponse odometer = this.subject.odometer();

    Assert.assertEquals(res, odometer);
  }

  @Test
  public void testFuel() throws Exception {
    double range = 1234;
    double percentRemaining = 0.43;
    double amountRemaining = 7;
    VehicleFuel expected = new VehicleFuel(range, percentRemaining, amountRemaining);
    String unitSystem = "metric";
    Date age = new Date();
    SmartcarResponse res = new SmartcarResponse<>(expected, unitSystem, age);

    PowerMockito.doReturn(res).when(this.subject).call("fuel", "GET", null, VehicleFuel.class);

    SmartcarResponse fuel = this.subject.fuel();

    Assert.assertEquals(res, fuel);
  }

  @Test
  public void testOil() throws Exception {
    double lifeRemaining = 0.86;
    VehicleOil expected = new VehicleOil(lifeRemaining);
    Date age = new Date();
    SmartcarResponse res = new SmartcarResponse<>(expected, null, age);

    PowerMockito.doReturn(res).when(this.subject).call("engine/oil", "GET", null, VehicleOil.class);

    SmartcarResponse fuel = this.subject.oil();

    Assert.assertEquals(res, fuel);
  }

  @Test
  public void testTirePressure() throws Exception {
    double frontLeft = 223.0;
    double frontRight = 218.0;
    double backLeft = 228.0;
    double backRight = 218.0;

    VehicleTirePressure expected = new VehicleTirePressure(frontLeft, frontRight, backLeft, backRight);
    String unitSystem = "metric";
    Date age = new Date();
    SmartcarResponse res = new SmartcarResponse<>(expected, unitSystem, age);

    PowerMockito.doReturn(res).when(this.subject).call("tires/pressure", "GET", null, VehicleTirePressure.class);

    SmartcarResponse tirePressure = this.subject.tirePressure();

    Assert.assertEquals(res, tirePressure);
  }

  @Test
  public void testBattery() throws Exception {
    double range = 1234;
    double percentRemaining = 0.43;
    VehicleBattery expected = new VehicleBattery(range, percentRemaining);
    String unitSystem = "metric";
    Date age = new Date();
    SmartcarResponse res = new SmartcarResponse<>(expected, unitSystem, age);

    PowerMockito.doReturn(res).when(this.subject).call("battery", "GET", null, VehicleBattery.class);

    SmartcarResponse battery = this.subject.battery();

    Assert.assertEquals(res, battery);
  }

  @Test
  public void testCharge() throws Exception {
    String state = "CHARGING";
    boolean isPluggedIn = true;
    VehicleCharge expected = new VehicleCharge(state, isPluggedIn);
    Date age = new Date();
    SmartcarResponse res = new SmartcarResponse<>(expected, null, age);

    PowerMockito.doReturn(res).when(this.subject).call("charge", "GET", null, VehicleCharge.class);

    SmartcarResponse charge = this.subject.charge();

    Assert.assertEquals(res, charge);
  }

  @Test
  public void testLocation() throws Exception {
    double expectedLongitude = 124.3;
    double expectedLatitude = 431.1;
    VehicleLocation expected = new VehicleLocation(expectedLongitude, expectedLatitude);
    String unitSystem = "metric";
    Date age = new Date();
    SmartcarResponse res = new SmartcarResponse<>(expected, unitSystem, age);

    PowerMockito.doReturn(res).when(this.subject).call("location", "GET", null, VehicleLocation.class);

    SmartcarResponse location = this.subject.location();

    Assert.assertEquals(res, location);
  }

  @Test
  public void testUnlock() throws Exception {

    ApiData data = new ApiData();
    SmartcarResponse res = new SmartcarResponse(data);
    JsonObject json = Json.createObjectBuilder()
      .add("action", "UNLOCK")
      .build();
    RequestBody body = RequestBody.create(Vehicle.JSON, json.toString());

    PowerMockito.doReturn(res.toString())
      .when(this.subject, "call", eq("security"), eq("POST"), refEq(body));

    this.subject.unlock();
  }

  @Test
  public void testLock() throws Exception {

    ApiData data = new ApiData();
    SmartcarResponse res = new SmartcarResponse(data);
    JsonObject json = Json.createObjectBuilder()
      .add("action", "LOCK")
      .build();
    RequestBody body = RequestBody.create(Vehicle.JSON, json.toString());


    PowerMockito.doReturn(res.toString())
      .when(this.subject, "call", eq("security"), eq("POST"), refEq(body));

    this.subject.lock();
  }

  @Test
  public void testBatch() throws Exception {
    JsonObject json = Json.createObjectBuilder()
      .add("headers", Json.createObjectBuilder()
          .add("sc-unit-system", "metric"))
      .add("requests", Json.createArrayBuilder()
          .add(Json.createObjectBuilder()
            .add("path", "/odometer")
            .build()
          )
          .add(Json.createObjectBuilder()
            .add("path", "/fuel")
            .build()
          )
          .build()
      )
      .build();
    RequestBody body = RequestBody.create(Vehicle.JSON, json.toString());
    String expectedJson = "[{\"headers\":{\"unitSystem\":\"metric\"},\"path\":\"/odometer\",\"code\":200,\"body\":{\"distance\":32768}}]";
    JsonParser parser = new JsonParser();
    JsonElement parsed = parser.parse(expectedJson);
    BatchResponse expectedBatch = new BatchResponse(parsed.getAsJsonArray());
    SmartcarResponse<BatchResponse> res = new SmartcarResponse(expectedBatch);
    ArrayList<String> paths = new ArrayList<>();
    paths.add("/odometer");
    PowerMockito.doReturn(res).when(this.subject, "call", eq("batch"), eq("POST"), refEq(body), refEq(BatchResponse.class));

    BatchResponse batch = this.subject.batch(paths);

    SmartcarResponse<VehicleOdometer> odo = batch.odometer();
    VehicleOdometer expectedOdo = new VehicleOdometer(32768);
    Assert.assertEquals(odo.getData().toString(), expectedOdo.toString());
  }

  @Test
  public void testBatchGetError() throws Exception {
     JsonObject json = Json.createObjectBuilder()
      .add("headers", Json.createObjectBuilder()
          .add("sc-unit-system", "metric"))
      .add("requests", Json.createArrayBuilder()
          .add(Json.createObjectBuilder()
            .add("path", "/odometer")
            .build()
          )
          .build()
      )
      .build();
    RequestBody body = RequestBody.create(Vehicle.JSON, json.toString());
    String expectedJson = "[{\"headers\":{\"unitSystem\":\"metric\"},\"path\":\"/odometer\",\"code\":200,\"body\":{\"distance\":32768}}]";
    JsonParser parser = new JsonParser();
    JsonElement parsed = parser.parse(expectedJson);
    BatchResponse expectedBatch = new BatchResponse(parsed.getAsJsonArray());
    SmartcarResponse<BatchResponse> res = new SmartcarResponse(expectedBatch);
    ArrayList<String> paths = new ArrayList<>();
    paths.add("/odometer");
    PowerMockito.doReturn(res).when(this.subject, "call", eq("batch"), eq("POST"), refEq(body), refEq(BatchResponse.class));

    BatchResponse batch = this.subject.batch(paths);

    Assert.assertThrows(SmartcarException.class, () -> batch.tirePressure());
  }

  @Test
  public void testBatchHTTPError() throws Exception {
     JsonObject json = Json.createObjectBuilder()
      .add("headers", Json.createObjectBuilder()
          .add("sc-unit-system", "metric"))
      .add("requests", Json.createArrayBuilder()
          .add(Json.createObjectBuilder()
            .add("path", "/sunroof")
            .build()
          )
          .build()
      )
      .build();
    RequestBody body = RequestBody.create(Vehicle.JSON, json.toString());
    String expectedJson = "[{\"headers\":{\"unitSystem\":\"metric\"},\"path\":\"/odometer\",\"code\":409,\"body\":{\"error\":\"vehicle_state_error\", \"message\": \"Vehicle state cannot be determined.\", \"code\" : \"VS_000\"}}]";
    JsonParser parser = new JsonParser();
    JsonElement parsed = parser.parse(expectedJson);
    BatchResponse expectedBatch = new BatchResponse(parsed.getAsJsonArray());
    SmartcarResponse<BatchResponse> res = new SmartcarResponse(expectedBatch);
    ArrayList<String> paths = new ArrayList<>();
    paths.add("/odometer");
    PowerMockito.doReturn(res).when(this.subject, "call", eq("batch"), eq("POST"), refEq(body), refEq(BatchResponse.class));

    BatchResponse batch = this.subject.batch(paths);

    try {
      batch.odometer();
    } catch(SmartcarException e) {
      Assert.assertEquals(e.getStatusCode(), 409);
      Assert.assertEquals(e.getMessage(), "Vehicle state cannot be determined.");
      Assert.assertEquals(e.getError(), "vehicle_state_error");
      Assert.assertEquals(e.getCode(), "VS_000");
    }
  }
}
