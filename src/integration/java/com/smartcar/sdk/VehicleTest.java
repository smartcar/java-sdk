package com.smartcar.sdk;

import com.smartcar.sdk.data.*;

import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/** Integration Test Suite: /vehicles/:id */
@PowerMockIgnore("javax.net.ssl.*")
public class VehicleTest extends IntegrationTest {
  private Vehicle vehicle;
  private Vehicle eVehicle;

  /**
   * Authenticates with the Smartcar platform and initializes a vehicle.
   *
   * @throws Exception
   */
  @BeforeSuite
  public void beforeSuite() throws Exception {
    // we need both types of vehicles to get full coverage of all endpoints
    this.vehicle = this.getVehicle("CHEVROLET");
    this.eVehicle = this.getVehicle("TESLA");
  }

  /** Tests that vehicle info can be obtained. */
  @Test(groups = "vehicle")
  public void testInfo() throws SmartcarException {
    VehicleAttributes info = this.vehicle.attributes();
  }

  /** Tests that the vehicle VIN can be obtained. */
  @Test(groups = "vehicle")
  public void testVin() throws SmartcarException {
    this.vehicle.vin();
  }

  /** Tests that the vehicle permissions can be obtained. */
  @Test(groups = "vehicle")
  public void testPermissions() throws SmartcarException {
    this.vehicle.permissions();
  }

  /** Tests permissions with paging */
  @Test(groups = "vehicle")
  public void testPermissionPaging() throws SmartcarException {
    RequestPaging paging = new RequestPaging(2, 2);
    ApplicationPermissions perms = this.vehicle.permissions(paging);
    String[] permissions = perms.getPermissions();
    Assert.assertTrue(permissions.length > 0);
  }

  /** Tests that the vehicle correctly handles imperial headers. */
  @Test(groups = "vehicle")
  public void testImperialHeaders() throws SmartcarException {
    this.vehicle.setUnitSystem(Vehicle.UnitSystem.IMPERIAL);
    VehicleOdometer response = this.vehicle.odometer();
    Assert.assertEquals(response.getMeta().getUnitSystem(), "imperial");
  }

  /** Tests that the vehicle correctly handles age headers. */
  @Test(groups = "vehicle")
  public void testAgeHeaders() throws SmartcarException {
    VehicleOdometer response = this.vehicle.odometer();
    Assert.assertTrue(response.getMeta().getDataAge() != null);
  }

  /** Tests that the vehicle correctly handles imperial headers. */
  @Test(groups = "vehicle")
  public void testRequestIdHeader() throws SmartcarException {
    VehicleOdometer response = this.vehicle.odometer();
    // Request ID is a UUID (36 characters)
    Assert.assertEquals(response.getMeta().getRequestId().length(), 36);
  }

  /** Tests that the odometer value can be obtained. */
  @Test(groups = "vehicle")
  public void testOdometer() throws SmartcarException {
    VehicleOdometer response = this.vehicle.odometer();
  }

  /** Tests that the fuel status can be obtained. */
  @Test(groups = "vehicle")
  public void testFuel() throws SmartcarException {
    VehicleFuel response = this.vehicle.fuel();
  }

  /** Tests that the battery status can be obtained. */
  @Test(groups = "vehicle")
  public void testBattery() throws SmartcarException {
    VehicleBattery response = this.vehicle.battery();
  }

  /** Tests that the battery capacity can be obtained. */
  @Test(groups = "vehicle")
  public void testBatteryCapacity() throws SmartcarException {
    VehicleBatteryCapacity response = this.vehicle.batteryCapacity();
  }

  /** Tests that the charging status can be obtained. */
  @Test(groups = "vehicle")
  public void testCharge() throws SmartcarException {
    VehicleCharge response = this.vehicle.charge();
  }

  /** Tests that the vehicle's location can be obtained. */
  @Test(groups = "vehicle")
  public void testLocation() throws SmartcarException {
    VehicleLocation response = this.vehicle.location();
  }

  /** Tests that the vehicle's oil status can be obtained. */
  @Test(groups = "vehicle")
  public void testOil() throws SmartcarException {
    VehicleOil response = this.vehicle.engineOil();
  }

  /** Tests that the vehicle's tire pressure can be obtained. */
  @Test(groups = "vehicle")
  public void testTirePressure() throws SmartcarException {
    VehicleTirePressure response = this.vehicle.tirePressure();
  }

  /** Tests that the vehicle lock action works. */
  @Test(groups = "vehicle")
  public void testActionLock() throws SmartcarException {
    this.vehicle.lock();
  }

  /** Tests that the vehicle unlock action works. */
  @Test(groups = "vehicle")
  public void testActionUnlock() throws SmartcarException {
    this.vehicle.unlock();
  }

  /** Tests that the vehicle start charge action works. */
  @Test(groups = "vehicle")
  public void testActionStartCharge() throws SmartcarException {
    this.eVehicle.startCharge();
  }

  /** Tests that the vehicle stop charge action works. */
  @Test(groups = "vehicle")
  public void testActionStopCharge() throws SmartcarException {
    this.eVehicle.stopCharge();
  }

  /** Tests that the batch request method works. */
  @Test(groups = "vehicle")
  public void testBatch() throws SmartcarException {
    String[] paths = {"/fuel", "/odometer"};
    BatchResponse response = this.vehicle.batch(paths);

    VehicleOdometer odo = response.odometer();
    VehicleFuel fuel = response.fuel();
  }

  /** Tests that the batch response headers are set properly. */
  @Test(groups = "vehicle")
  public void testBatchResponseHeaders() throws SmartcarException {
    this.vehicle.setUnitSystem(Vehicle.UnitSystem.IMPERIAL);
    String[] paths = {"/odometer"};
    BatchResponse response = this.vehicle.batch(paths);
    Assert.assertEquals(response.getRequestId().length(), 36);

    VehicleOdometer odo = response.odometer();
    Assert.assertEquals(odo.getMeta().getUnitSystem(), "imperial");
    Assert.assertEquals(odo.getMeta().getRequestId().length(), 36);
    Assert.assertTrue(odo.getMeta().getDataAge() != null);
  }

  /** Tests that access for the current application can be revoked. */
  @Test(dependsOnGroups = "vehicle")
  public void testDisconnect() throws SmartcarException {
    this.vehicle.disconnect();
  }
}
