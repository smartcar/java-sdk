package com.smartcar.sdk;

import com.smartcar.sdk.data.*;
import com.smartcar.sdk.helpers.AuthHelpers;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class SmartcarTest {
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
