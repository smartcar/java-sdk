package com.smartcar.sdk;

import com.smartcar.sdk.data.Compatibility;
import com.smartcar.sdk.data.RequestPaging;
import com.smartcar.sdk.data.User;
import com.smartcar.sdk.data.VehicleIds;
import com.smartcar.sdk.helpers.AuthHelpers;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class SmartcarTest {
    private String accessToken;
    private String clientId = System.getenv("E2E_SMARTCAR_CLIENT_ID");
    private String clientSecret = System.getenv("E2E_SMARTCAR_CLIENT_SECRET");
    private AuthClient client;
    private String authorizeUrl;
    private String[] scope = {"read_odometer"};

    @BeforeSuite
    public void beforeSuite() throws Exception {
        this.client = AuthHelpers.getConfiguredAuthClientBuilder().build();
        this.authorizeUrl = client.authUrlBuilder(new String[] {"read_vehicle_info"}).build();
        String code = AuthHelpers.runAuthFlow(client.authUrlBuilder(this.scope).build());
        this.accessToken = client.exchangeCode(code).getAccessToken();
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
        String[] scope = {"read_odometer"};

        SmartcarCompatibilityRequest request =  new SmartcarCompatibilityRequest.Builder()
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .vin(vin)
                .scope(scope)
                .build();
        Compatibility comp = Smartcar.getCompatibility(request);
        Assert.assertTrue(comp.getCompatible());
    }
}
