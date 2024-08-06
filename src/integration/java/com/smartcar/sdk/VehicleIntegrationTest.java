package com.smartcar.sdk;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

import com.smartcar.sdk.data.*;
import com.smartcar.sdk.helpers.AuthHelpers;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * Integration Test Suite: /vehicles/:id
 */
@PowerMockIgnore("javax.net.ssl.*")
public class VehicleIntegrationTest {
    private Vehicle vehicle;
    private Vehicle eVehicle;

    private Vehicle getVehicle(String make, String[] scope) throws Exception {
        AuthClient client = AuthHelpers.getConfiguredAuthClientBuilder().build();
        String code = AuthHelpers.runAuthFlow(client.authUrlBuilder(scope).build(), make);
        String accessToken = client.exchangeCode(code).getAccessToken();
        String[] vehiclesIds = Smartcar.getVehicles(accessToken).getVehicleIds();
        return new Vehicle(vehiclesIds[0], accessToken);
    }

    @BeforeSuite
    public void beforeSuite() throws Exception {
        this.vehicle = this.getVehicle("CHEVROLET", AuthHelpers.DEFAULT_SCOPE);
        this.eVehicle =
                this.getVehicle(
                        "FORD",
                        new String[]{
                                "required:control_charge",
                                "required:control_security",
                                "required:control_navigation",
                                "read_battery",
                                "read_charge"
                        });
    }

    /**
     * Tests that vehicle info can be obtained.
     */
    @Test(groups = "vehicle")
    public void testInfo() throws SmartcarException {
        VehicleAttributes info = this.vehicle.attributes();
    }

    /**
     * Tests that the vehicle VIN can be obtained.
     */
    @Test(groups = "vehicle")
    public void testVin() throws SmartcarException {
        this.vehicle.vin();
    }

    /**
     * Tests that the vehicle permissions can be obtained.
     */
    @Test(groups = "vehicle")
    public void testPermissions() throws SmartcarException {
        this.vehicle.permissions();
    }

    /**
     * Tests permissions with paging
     */
    @Test(groups = "vehicle")
    public void testPermissionPaging() throws SmartcarException {
        RequestPaging paging = new RequestPaging(2, 2);
        ApplicationPermissions perms = this.vehicle.permissions(paging);
        String[] permissions = perms.getPermissions();
        Assert.assertTrue(permissions.length > 0);
        Assert.assertTrue(perms.getPaging().getCount() > 0);
        Assert.assertTrue(perms.getPaging().getOffset() >= 0);
    }

    /**
     * Tests that the vehicle correctly handles imperial headers.
     */
    @Test(groups = "vehicle")
    public void testImperialHeaders() throws SmartcarException {
        this.vehicle.setUnitSystem(Vehicle.UnitSystem.IMPERIAL);
        VehicleOdometer response = this.vehicle.odometer();
        Assert.assertEquals(response.getMeta().getUnitSystem(), "imperial");
    }

    /**
     * Tests that the vehicle correctly handles age headers.
     */
    @Test(groups = "vehicle")
    public void testAgeHeaders() throws SmartcarException {
        VehicleOdometer response = this.vehicle.odometer();
        Assert.assertTrue(response.getMeta().getDataAge() != null);
    }

    /**
     * Tests that the vehicle correctly handles request id headers.
     */
    @Test(groups = "vehicle")
    public void testRequestIdHeader() throws SmartcarException {
        VehicleOdometer response = this.vehicle.odometer();
        Assert.assertNotNull(response.getMeta());
        Assert.assertNotNull(response.getMeta().getRequestId());
        // Request ID is a UUID (36 characters)
        Assert.assertEquals(response.getMeta().getRequestId().length(), 36);
    }

    /**
     * Tests that the odometer value can be obtained.
     */
    @Test(groups = "vehicle")
    public void testOdometer() throws SmartcarException {
        VehicleOdometer response = this.vehicle.odometer();
    }

    /**
     * Tests that the fuel status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testFuel() throws SmartcarException {
        VehicleFuel response = this.vehicle.fuel();
    }

    /**
     * Tests that the battery status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testBattery() throws SmartcarException {
        VehicleBattery response = this.vehicle.battery();
    }

    /**
     * Tests that the battery capacity can be obtained.
     */
    @Test(groups = "vehicle")
    public void testBatteryCapacity() throws SmartcarException {
        VehicleBatteryCapacity response = this.vehicle.batteryCapacity();
    }

    /**
     * Tests that the charging status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testCharge() throws SmartcarException {
        VehicleCharge response = this.vehicle.charge();
    }


    /**
     * Tests that the charging status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testGetChargeLimit() throws SmartcarException {
        VehicleChargeLimit response = this.eVehicle.getChargeLimit();
    }

    /**
     * Tests that the charging status can be set.
     */
    @Test(groups = "vehicle")
    public void testSetChargeLimit() throws SmartcarException {
        ActionResponse response = this.eVehicle.setChargeLimit(0.7);
    }

    /**
     * Tests that the vehicle's location can be obtained.
     */
    @Test(groups = "vehicle")
    public void testLocation() throws SmartcarException {
        VehicleLocation response = this.vehicle.location();
    }

    /**
     * Tests that the vehicle's oil status can be obtained.
     */
    @Test(groups = "vehicle")
    public void testOil() throws SmartcarException {
        VehicleEngineOil response = this.vehicle.engineOil();
    }

    /**
     * Tests that the vehicle's tire pressure can be obtained.
     */
    @Test(groups = "vehicle")
    public void testTirePressure() throws SmartcarException {
        VehicleTirePressure response = this.vehicle.tirePressure();
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
     * Tests that the vehicle start charge action works.
     */
    @Test(groups = "vehicle")
    public void testActionStartCharge() throws SmartcarException {
        this.eVehicle.startCharge();
    }

    /**
     * Tests that the vehicle stop charge action works.
     */
    @Test(groups = "vehicle")
    public void testActionStopCharge() throws SmartcarException {
        this.eVehicle.stopCharge();
    }

    /**
     * Test that the vehicle sendDestination action works.
     */
    // @Test(groups = "vehicle")
    // public void testActionSendDestination() throws SmartcarException {
    //     this.eVehicle.sendDestination(47.6205063, -122.3518523);
    // }

    /**
     * Tests that the batch request method works.
     */
    @Test(groups = "vehicle")
    public void testBatch() throws SmartcarException {
        String[] paths = {
                "/", "/location", "/odometer", "/engine/oil", "/vin",
                "/tires/pressure", "/fuel"
        };
        String[] evPaths = {
                "/battery", "/battery/capacity", "/charge", "/charge/limit"
        };
        BatchResponse response = this.vehicle.batch(paths);
        VehicleAttributes attr = response.attributes();
        Assert.assertNotNull(attr.getId());
        Assert.assertNotNull(attr.getMake());
        Assert.assertNotNull(attr.getModel());
        Assert.assertTrue(attr.getYear() > 0);
        VehicleLocation location = response.location();
        Assert.assertTrue(location.getLatitude() > -90);
        Assert.assertTrue(location.getLongitude() > -180);
        VehicleEngineOil oil = response.engineOil();
        Assert.assertTrue(oil.getLifeRemaining() > 0);
        VehicleVin vin = response.vin();
        Assert.assertNotNull(vin.getVin());
        VehicleOdometer odo = response.odometer();
        Assert.assertTrue(odo.getDistance() > 0);
        VehicleFuel fuel = response.fuel();
        Assert.assertTrue(fuel.getAmountRemaining() > 0);
        Assert.assertTrue(fuel.getRange() > 0);
        Assert.assertTrue(fuel.getPercentRemaining() > 0);
        VehicleTirePressure pressure = response.tirePressure();
        Assert.assertTrue(pressure.getBackLeft() > 0);
        Assert.assertTrue(pressure.getBackRight() > 0);
        Assert.assertTrue(pressure.getFrontLeft() > 0);
        Assert.assertTrue(pressure.getFrontRight() > 0);

        BatchResponse evResponse = this.eVehicle.batch(evPaths);
        VehicleBattery bat = evResponse.battery();
        Assert.assertTrue(bat.getPercentRemaining() > 0);
        Assert.assertTrue(bat.getRange() > 0);

        VehicleCharge charge = evResponse.charge();
        boolean plugged = charge.getIsPluggedIn();
        VehicleBatteryCapacity cap = evResponse.batteryCapacity();
        Assert.assertTrue(cap.getCapacity() > 0);

        VehicleChargeLimit chargeLimit = evResponse.chargeLimit();
        Assert.assertTrue(chargeLimit.getChargeLimit() > 0);

    }

    /**
     * Tests that the batch response headers are set properly.
     */
    @Test(groups = "vehicle")
    public void testBatchResponseHeaders() throws SmartcarException {
        this.vehicle.setUnitSystem(Vehicle.UnitSystem.IMPERIAL);
        String[] paths = {"/odometer"};
        BatchResponse response = this.vehicle.batch(paths);
        Assert.assertEquals(response.getRequestId().length(), 36);

        VehicleOdometer odo = response.odometer();
        Assert.assertEquals(odo.getMeta().getUnitSystem(), "imperial");
        Assert.assertEquals(odo.getMeta().getRequestId().length(), 36);
        Assert.assertNotNull(odo.getMeta().getDataAge());
    }

    /**
     * Tests that the request method works for get request.
     */
    @Test(groups = "vehicle")
    public void testVehicleRequest() throws Exception {
        SmartcarVehicleRequest request = new SmartcarVehicleRequest.Builder()
                .method("GET")
                .path("odometer")
                .addHeader("sc-unit-system", "imperial")
                .build();

        VehicleResponse odometer = this.vehicle.request(request);

        Assert.assertEquals(odometer.getBody().get("distance").getClass(), JsonPrimitive.class);
        Assert.assertNotNull(odometer.getMeta().getRequestId());
        Assert.assertEquals(odometer.getMeta().getUnitSystem(), "imperial");
    }

    /**
     * Tests request method with post request & body.
     */
    @Test(groups = "vehicle")
    public void testRequestWithBody() throws Exception {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        builder.add(Json.createObjectBuilder().add("path", "/odometer").build());
        builder.add(Json.createObjectBuilder().add("path", "/vin").build());
        JsonArray paths = builder.build();

        SmartcarVehicleRequest request =
                new SmartcarVehicleRequest.Builder()
                        .method("POST")
                        .path("batch")
                        .addBodyParameter("requests", paths)
                        .build();

        VehicleResponse response = this.vehicle.request(request);
        com.google.gson.JsonArray responses =
                ((com.google.gson.JsonArray) response.getBody().get("responses"));

        JsonObject odo = responses.get(0).getAsJsonObject();
        JsonObject vin = responses.get(1).getAsJsonObject();
        Assert.assertEquals(odo.get("path").getAsString(), "/odometer");
        Assert.assertEquals(odo.get("code").getAsString(), "200");
        Assert.assertNotNull(odo.get("body").getAsJsonObject().get("distance"));

        Assert.assertEquals(vin.get("path").getAsString(), "/vin");
        Assert.assertEquals(vin.get("code").getAsString(), "200");
        Assert.assertNotNull(vin.get("body").getAsJsonObject().get("vin"));
        Assert.assertEquals(response.getMeta().getRequestId().length(), 36);
    }

    /**
     * Tests that access for the current application can be revoked.
     */
    @AfterTest
    public void testDisconnect() throws SmartcarException {
        this.vehicle.disconnect();
    }

    @Test(dependsOnGroups = "vehicle")
    public void testSubscribeUnsubscribe() throws SmartcarException {
        this.vehicle.subscribe(AuthHelpers.getWebhookId());

        this.vehicle.unsubscribe(
                AuthHelpers.getApplicationManagementToken(), AuthHelpers.getWebhookId());
    }
}
