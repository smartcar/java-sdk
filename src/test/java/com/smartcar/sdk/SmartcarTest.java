package com.smartcar.sdk;


import com.smartcar.sdk.data.Compatibility;
import com.smartcar.sdk.data.User;
import com.smartcar.sdk.data.VehicleIds;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;


public class SmartcarTest {
    private final String sampleClientId = "cl13nt1d-t35t-46dc-aa25-bdd042f54e7d";
    private final String sampleClientSecret = "24d55382-843f-4ce9-a7a7-cl13nts3cr3t";
    private final String sampleRequestId = "2eddd02f-8aaa-2eee-bfff-012345678901";
    private final String sampleRedirectUri = "https://example.com/";
    private final String sampleRedirectUriEncoded = "https%3A%2F%2Fexample.com%2F";
    private final String[] sampleScope = {"read_vehicle_info", "read_location", "read_odometer"};
    private final boolean sampleTestMode = true;
    private String fakeAccessToken = "F4K3_4CC355_T0K3N";
    private MockWebServer mockWebServer = new MockWebServer();

    @BeforeSuite
    private void beforeSuite() throws IOException {
        Smartcar.API_ORIGIN = "http://" + this.mockWebServer.getHostName() + ":" + this.mockWebServer.getPort();
        this.mockWebServer.start(8888);
    }

    @AfterSuite
    private void afterSuite() throws IOException {
        this.mockWebServer.shutdown();
    }
    
    @Test
    public void testGetUser() throws Exception {
        String expectedUserId = "9c58a58f-579e-4fce-b2fc-53a518271b8c";
        MockResponse response = new MockResponse()
                .setBody("{ \"id\": \"" + expectedUserId + "\" }")
                .addHeader("sc-request-id", this.sampleRequestId);
        this.mockWebServer.enqueue(response);

        User user = Smartcar.getUser(this.fakeAccessToken);
        Assert.assertEquals(user.getId(), expectedUserId);
        Assert.assertEquals(user.getMeta().getRequestId(), this.sampleRequestId);
    }

    @Test
    public void testVehicleIds() throws Exception {
        String vehicleId = "9c58a58f-579e-4fce-b2fc-53a518271b8c";

        MockResponse response = new MockResponse()
                .setBody("{ \"paging\": {\"count\": 1, \"offset\": 0 }, \"vehicles\": [\"" + vehicleId + "\"] }")
                .addHeader("sc-request-id", this.sampleRequestId);
        this.mockWebServer.enqueue(response);

        VehicleIds vehicleIds = Smartcar.getVehicles(this.fakeAccessToken);

        String[] vIds = vehicleIds.getVehicleIds();
        Assert.assertNotNull(vIds);
        Assert.assertEquals(vIds[0], vehicleId);
    }

    @Test
    public void testGetCompatibility() throws Exception {
        String vin = "";
        String scope[] = {"read_odometer"};

        MockResponse response = new MockResponse()
                .setBody("{ \"compatible\": true }")
                .addHeader("sc-request-id", this.sampleRequestId);
        this.mockWebServer.enqueue(response);

        SmartcarCompatibilityRequest request =  new SmartcarCompatibilityRequest.Builder().vin(vin).scope(scope).build();
        Compatibility comp = Smartcar.getCompatibility(request);
        Assert.assertEquals(comp.getCompatible(), true);
        Assert.assertEquals(comp.getMeta().getRequestId(), this.sampleRequestId);
    }

    /**
     * Tests setting the api version to 2.0 and getting the api url that is used for subsequent
     * requests
     */

    @Test
    public void testSetApiVersion() {
        Smartcar.setApiVersion("2.0");
        String url = Smartcar.getApiUrl();
        Assert.assertEquals(url, "https://api.smartcar.com/v2.0");
        Smartcar.setApiVersion("1.0");
    }
}
