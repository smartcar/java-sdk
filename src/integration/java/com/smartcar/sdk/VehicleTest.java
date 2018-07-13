package com.smartcar.sdk;

import com.smartcar.sdk.data.SmartcarResponse;
import com.smartcar.sdk.data.VehicleInfo;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Integration Test Suite: /vehicles/:id
 */
public class VehicleTest extends IntegrationTest {
    private Vehicle vehicle;

    @BeforeSuite
    public void beforeSuite() throws Exception {
        String[] vehicleIds = AuthClient.getVehicleIds(this.getAuth().getAccessToken()).getData().getVehicleIds();
        this.vehicle = new Vehicle(vehicleIds[0], this.getAuth().getAccessToken());
    }

    /**
     * Tests that vehicle info can be obtained.
     */
    @Test
    public void testInfo() throws SmartcarException {
        VehicleInfo info = this.vehicle.info();
    }

    /**
     * Tests that the vehicle VIN can be obtained.
     */
    @Test
    public void testVin() throws SmartcarException {
        String vin = this.vehicle.vin();
    }

    /**
     * Tests that the application permissions can be obtained.
     */
    @Test
    public void testPermissions() throws SmartcarException {
        String[] permissions = this.vehicle.permissions();
    }

    /**
     * Tests that the odometer value can be obtained.
     */
    @Test
    public void testOdometer() throws SmartcarException {
        SmartcarResponse response = this.vehicle.odometer();
    }

    /**
     * Tests that the vehicle's location can be obtained.
     */
    @Test
    public void testLocation() throws SmartcarException {
        SmartcarResponse response = this.vehicle.location();
    }

    /**
     * Tests that the vehicle lock action works.
     */
    @Test
    public void testActionLock() throws SmartcarException {
        this.vehicle.lock();
    }

    /**
     * Tests that the vehicle unlock action works.
     */
    @Test
    public void testActionUnlock() throws SmartcarException {
        this.vehicle.unlock();
    }

    /**
     * Tests that access for the current application can be revoked.
     */
//    @Test
    public void testDisconnect() throws SmartcarException {
        this.vehicle.disconnect();
    }
}
