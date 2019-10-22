package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import com.smartcar.sdk.data.BatchResponse;
import com.smartcar.sdk.data.SmartcarResponse;
import com.smartcar.sdk.data.VehicleFuel;
import com.smartcar.sdk.data.VehicleIds;
import com.smartcar.sdk.data.VehicleInfo;
import com.smartcar.sdk.data.VehicleOdometer;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * Integration Test Suite: /vehicles/:id
 */
public class VehicleTest extends IntegrationTest {
    private Vehicle vehicle;

    /**
     * Authenticates with the Smartcar platform and initializes
     * a vehicle.
     *
     * @throws Exception
     */
    @BeforeSuite
    public void beforeSuite() throws Exception {
        Auth auth = this.getAuth();
        String accessToken = auth.getAccessToken();

        SmartcarResponse vehicleIdResponse = AuthClient.getVehicleIds(accessToken);
        VehicleIds vehicleIdData = (VehicleIds) vehicleIdResponse.getData();
        String[] vehicleIds = vehicleIdData.getVehicleIds();

        this.vehicle = new Vehicle(vehicleIds[0], this.getAuth().getAccessToken());
    }

    /**
     * Tests that vehicle info can be obtained.
     */
    @Test(groups = "vehicle")
    public void testInfo() throws SmartcarException {
        VehicleInfo info = this.vehicle.info();
    }

    /**
     * Tests that the vehicle VIN can be obtained.
     */
    @Test(groups = "vehicle")
    public void testVin() throws SmartcarException {
        String vin = this.vehicle.vin();
    }

    /**
     * Tests that the vehicle permissions can be obtained.
     */
    @Test(groups = "vehicle")
    public void testPermissions() throws SmartcarException {
        String[] permissions = this.vehicle.permissions();
    }

    /**
     * Tests that the vehicle correctly handles imperial headers.
     */
    @Test(groups = "vehicle")
    public void testImperialHeaders() throws SmartcarException {
        this.vehicle.setUnitSystem(Vehicle.UnitSystem.IMPERIAL);
        SmartcarResponse response = this.vehicle.odometer();
        Assert.assertEquals(response.getUnitSystem(), "imperial");
    }

    /**
     * Tests that the vehicle has certain permissions.
     */
    @Test(groups = "vehicle")
    public void testHasPermissions() throws SmartcarException {
        Assert.assertTrue(this.vehicle.hasPermissions("required:read_odometer"));
        Assert.assertTrue(this.vehicle.hasPermissions(new String[] {"read_odometer", "required:read_location"}));

        Assert.assertFalse(this.vehicle.hasPermissions("read_ignition"));
        Assert.assertFalse(this.vehicle.hasPermissions(new String[] {"read_odometer", "read_ignition"}));
    }

    /**
     * Tests that the odometer value can be obtained.
     */
    @Test(groups = "vehicle")
    public void testOdometer() throws SmartcarException {
        SmartcarResponse response = this.vehicle.odometer();
    }

    /**
     * Tests that the fuel status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testFuel() throws SmartcarException {
        SmartcarResponse response = this.vehicle.fuel();
    }

    /**
     * Tests that the battery status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testBattery() throws SmartcarException {
        SmartcarResponse response = this.vehicle.battery();
    }

    /**
     * Tests that the charging status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testCharge() throws SmartcarException {
        SmartcarResponse response = this.vehicle.charge();
    }

    /**
     * Tests that the vehicle's location can be obtained.
     */
    @Test(groups = "vehicle")
    public void testLocation() throws SmartcarException {
        SmartcarResponse response = this.vehicle.location();
    }

    /**
     * Tests that the vehicle's oil status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testOil() throws SmartcarException {
        SmartcarResponse response = this.vehicle.oil();
    }

     /**
     * Tests that the vehicle's tire pressure can be obtained.
     */
    @Test(groups = "vehicle")
    public void testTirePressure() throws SmartcarException {
        SmartcarResponse response = this.vehicle.tirePressure();
    }

    /**
     * Tests that the vehicle lock action works.
     */
    @Test(groups = "vehicle")
    public void testActionLock() throws SmartcarException {
        this.vehicle.lock();
    }

    /**
     * Tests that the vehicle unlock action works.
     */
    @Test(groups = "vehicle")
    public void testActionUnlock() throws SmartcarException {
        this.vehicle.unlock();
    }

    /**
     * Tests that the batch request method works.
     */
    @Test(groups = "vehicle")
    public void testBatch() throws SmartcarException {
        String[] paths = {"/fuel", "/odometer"};
        BatchResponse response = this.vehicle.batch(paths);

        SmartcarResponse<VehicleOdometer> odo = response.odometer();
        SmartcarResponse<VehicleFuel> fuel = response.fuel();
    }

    /**
     * Tests that access for the current application can be revoked.
     */
    @Test(dependsOnGroups = "vehicle")
    public void testDisconnect() throws SmartcarException {
        this.vehicle.disconnect();
    }
}
