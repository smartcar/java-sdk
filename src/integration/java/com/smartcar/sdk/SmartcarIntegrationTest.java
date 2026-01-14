package com.smartcar.sdk;

import com.smartcar.sdk.data.*;
import com.smartcar.sdk.data.v3.VehicleAttributes;
import com.smartcar.sdk.helpers.AuthHelpers;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SmartcarIntegrationTest {
    private static final String V3_VEHICLE_ID = "tst2e255-d3c8-4f90-9fec-e6e68b98e9cb";
    private static final String V3_TEST_TOKEN = "test-data-token";

    private String accessToken;
    private VehicleIds vehicleIds;
    private String clientId;
    private String clientSecret;
    private String amt;
    private AuthClient client;
    private String authorizeUrl;
    private String[] scope = {"read_odometer"};

    @BeforeSuite
    public void beforeSuite() throws Exception {
        this.client = AuthHelpers.getConfiguredAuthClientBuilder().build();
        this.authorizeUrl = client.authUrlBuilder(new String[]{"read_vehicle_info"}).build();
        String code = AuthHelpers.runAuthFlow(client.authUrlBuilder(this.scope).build());
        // Add delay to prevent rate limiting
        Thread.sleep(5000);
        this.accessToken = client.exchangeCode(code).getAccessToken();
        VehicleIds vehicleIds = Smartcar.getVehicles(this.accessToken);
        this.vehicleIds = vehicleIds;
        this.clientId = AuthHelpers.getClientId();
        this.clientSecret = AuthHelpers.getClientSecret();
        this.amt = AuthHelpers.getApplicationManagementToken();
    }

    @Test
    public void testGetUsers() throws SmartcarException {
        User user = Smartcar.getUser(this.accessToken);
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void testGetVehicles() throws SmartcarException {
        VehicleIds vehicleIds = Smartcar.getVehicles(this.accessToken);
        Assert.assertNotNull(vehicleIds.getVehicleIds());
    }

    @Test
    public void testGetVehiclesPaging() throws SmartcarException {
        RequestPaging paging = new RequestPaging(1, 0);
        VehicleIds vehicleIds = Smartcar.getVehicles(this.accessToken, paging);
        Assert.assertEquals(vehicleIds.getVehicleIds().length, 1);
        Assert.assertTrue(vehicleIds.getPaging().getCount() > 0);
        Assert.assertEquals(vehicleIds.getPaging().getOffset(), 0);
    }

    @Test
    public void testGetVehicle() throws SmartcarException {
        VehicleAttributes vehicle = Smartcar.getVehicle(V3_TEST_TOKEN, V3_VEHICLE_ID);

        Assert.assertNotNull(vehicle);
        Assert.assertEquals(vehicle.getId(), V3_VEHICLE_ID);
        Assert.assertEquals(vehicle.getMake(), "TESLA");
        Assert.assertEquals(vehicle.getModel(), "Model Y");
        Assert.assertEquals(vehicle.getYear().intValue(), 2021);
    }

    @Test
    public void testGetCompatibility() throws Exception {
        String vin = "5YJSA1E29LF403082";
        String[] scope = {"read_odometer", "read_fuel"};
        String[] paths = {"/odometer", "/fuel"};
        String[] reasons = {null, "SMARTCAR_NOT_CAPABLE"};
        ArrayList<String> permissionList = new ArrayList<>(Arrays.asList(scope));
        ArrayList<String> endpointList = new ArrayList<>(Arrays.asList(paths));
        ArrayList<String> reasonsList = new ArrayList<>(Arrays.asList(reasons));

        SmartcarCompatibilityRequest request = new SmartcarCompatibilityRequest.Builder()
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .vin(vin)
                .scope(scope)
                .build();
        Compatibility comp = Smartcar.getCompatibility(request);
        Compatibility.Capability[] capabilities = comp.getCapabilities();
        Assert.assertTrue(comp.getCompatible());
        Assert.assertEquals(capabilities.length, 2);
        boolean capable = true;
        for (Compatibility.Capability capability : capabilities) {
            capable = capable && capability.getCapable();
            Assert.assertTrue(permissionList.contains(capability.getPermission()));
            Assert.assertTrue(endpointList.contains(capability.getEndpoint()));
            Assert.assertTrue(reasonsList.contains(capability.getReason()));
        }
        Assert.assertFalse((capable));
    }

    @Test
    public void testGetCompatibilityMatrix() throws Exception {
        String[] scope = {"read_battery", "read_charge"};

        SmartcarCompatibilityMatrixRequest request = new SmartcarCompatibilityMatrixRequest.Builder()
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .make("NISSAN")
                .type("BEV")
                .scope(scope)
                .build();
        CompatibilityMatrix matrix = Smartcar.getCompatibilityMatrix(request);
        Map<String, List<CompatibilityMatrix.CompatibilityEntry>> results = matrix.getResults();
        Assert.assertTrue(results.size() > 0);
        for (Map.Entry<String, List<CompatibilityMatrix.CompatibilityEntry>> entry : results.entrySet()) {
            for (CompatibilityMatrix.CompatibilityEntry result : entry.getValue()) {
                Assert.assertNotNull(result.getModel());
                Assert.assertNotNull(result.getStartYear());
                Assert.assertNotNull(result.getEndYear());
                Assert.assertNotNull(result.getType());
                Assert.assertNotNull(result.getEndpoints());
                Assert.assertNotNull(result.getPermissions());

                Assert.assertEquals(result.getType(), "BEV");
                List<String> permissions = Arrays.asList(result.getPermissions());
                Assert.assertTrue(permissions.containsAll(Arrays.asList(scope)));
            }
        }
    }

    // TODO uncomment when test mode connections are returned
    // @Test
    // public void testGetConnections() throws SmartcarException {
    //     String testVehicleId = this.vehicleIds.getVehicleIds()[0];
    //     ConnectionsFilter filter = new ConnectionsFilter
    //             .Builder()
    //             .vehicleId(testVehicleId)
    //             .build();
    //     GetConnections connections = Smartcar.getConnections(this.amt, filter);
    //     Assert.assertEquals(connections.getConnections().length, 1);
    // }

    // TODO uncomment when test mode connections are returned
    // @Test
    // public void testDeleteConnections() throws SmartcarException {
    //     String testVehicleId = this.vehicleIds.getVehicleIds()[0];
    //     ConnectionsFilter filter = new ConnectionsFilter
    //             .Builder()
    //             .vehicleId(testVehicleId)
    //             .build();
    //     DeleteConnections connections = Smartcar.deleteConnections(this.amt, filter);
    //     Assert.assertEquals(connections.getConnections().length, 1);
    // }
}
