package com.smartcar.sdk;

import com.smartcar.sdk.data.*;
import okhttp3.RequestBody;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
/**
 * Test Suite: Vehicle
 */
//@RunWith(PowerMockRunner.class)
@PrepareForTest(Vehicle.class)
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
}
