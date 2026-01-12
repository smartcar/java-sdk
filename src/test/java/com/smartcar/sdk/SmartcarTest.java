package com.smartcar.sdk;

import com.smartcar.sdk.data.Compatibility;
import com.smartcar.sdk.data.CompatibilityMatrix;
import com.smartcar.sdk.data.User;
import com.smartcar.sdk.data.VehicleIds;
import com.smartcar.sdk.data.v3.VehicleAttributes;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

@PowerMockIgnore({"javax.net.ssl.*", "javax.crypto.*"})
@PrepareForTest({
        Smartcar.class,
})
public class SmartcarTest extends PowerMockTestCase {
    private final String sampleClientId = "cl13nt1d-t35t-46dc-aa25-bdd042f54e7d";
    private final String sampleClientSecret = "24d55382-843f-4ce9-a7a7-cl13nts3cr3t";
    private final String sampleRequestId = "2eddd02f-8aaa-2eee-bfff-012345678901";
    private final String fakeAccessToken = "F4K3_4CC355_T0K3N";

    @Test
    public void testGetUser() throws Exception {
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("SMARTCAR_API_ORIGIN")).thenReturn(
                "http://localhost:" + TestExecutionListener.mockWebServer.getPort()
        );
        String expectedUserId = "9c58a58f-579e-4fce-b2fc-53a518271b8c";
        MockResponse response = new MockResponse()
                .setBody("{ \"id\": \"" + expectedUserId + "\" }")
                .addHeader("sc-request-id", this.sampleRequestId);
        TestExecutionListener.mockWebServer.enqueue(response);

        User user = Smartcar.getUser(this.fakeAccessToken);
        Assert.assertEquals(user.getId(), expectedUserId);
        Assert.assertEquals(user.getMeta().getRequestId(), this.sampleRequestId);
        TestExecutionListener.mockWebServer.takeRequest();
    }

    @Test
    public void testVehicles() throws Exception {
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("SMARTCAR_API_ORIGIN")).thenReturn(
                "http://localhost:" + TestExecutionListener.mockWebServer.getPort()
        );
        String vehicleId = "9c58a58f-579e-4fce-b2fc-53a518271b8c";

        MockResponse response = new MockResponse()
                .setBody("{ \"paging\": {\"count\": 1, \"offset\": 0 }, \"vehicles\": [\"" + vehicleId + "\"] }")
                .addHeader("sc-request-id", this.sampleRequestId);
        TestExecutionListener.mockWebServer.enqueue(response);

        VehicleIds vehicleIds = Smartcar.getVehicles(this.fakeAccessToken);

        String[] vIds = vehicleIds.getVehicleIds();
        Assert.assertNotNull(vIds);
        Assert.assertEquals(vIds[0], vehicleId);
        TestExecutionListener.mockWebServer.takeRequest();
    }

    @Test
    @PrepareForTest(System.class)
    public void testGetVehicle() throws Exception {
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("SMARTCAR_API_V3_ORIGIN")).thenReturn(
                "http://localhost:" + TestExecutionListener.mockWebServer.getPort()
        );

        String fileName = "src/test/resources/GetVehicle.json";
        String body = new String(Files.readAllBytes(Paths.get(fileName)));

        MockResponse response = new MockResponse()
                .setBody(body)
                .addHeader("sc-request-id", this.sampleRequestId);
        TestExecutionListener.mockWebServer.enqueue(response);

        String vehicleId = "36ab27d0-fd9d-4455-823a-ce30af709ffc";

        VehicleAttributes vehicle = Smartcar.getVehicle(this.fakeAccessToken, vehicleId);
        Assert.assertNotNull(vehicle);
        Assert.assertEquals(vehicle.getId(), vehicleId);
        Assert.assertEquals(vehicle.getMake(), "TESLA");
        Assert.assertEquals(vehicle.getModel(), "Model 3");
        Assert.assertEquals(vehicle.getYear().intValue(), 2019);
    }

    @Test
    @PrepareForTest(System.class)
    public void testGetCompatibility() throws Exception {
        String vin = "1234";
        String[] scope;
        scope = new String[]{"read_odometer"};

        MockResponse response = new MockResponse()
                .setBody("{ \"compatible\": true }")
                .addHeader("sc-request-id", this.sampleRequestId);
        TestExecutionListener.mockWebServer.enqueue(response);

        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("SMARTCAR_API_ORIGIN")).thenReturn(
                "http://localhost:" + TestExecutionListener.mockWebServer.getPort()
        );
        PowerMockito.when(System.getenv("SMARTCAR_CLIENT_ID")).thenReturn(this.sampleClientId);
        PowerMockito.when(System.getenv("SMARTCAR_CLIENT_SECRET")).thenReturn(this.sampleClientSecret);

        SmartcarCompatibilityRequest request =  new SmartcarCompatibilityRequest.Builder()
                .clientId(this.sampleClientId)
                .clientSecret(this.sampleClientSecret)
                .vin(vin)
                .scope(scope)
                .build();
        Compatibility comp = Smartcar.getCompatibility(request);
        Assert.assertTrue(comp.getCompatible());
        Assert.assertEquals(comp.getMeta().getRequestId(), this.sampleRequestId);
        RecordedRequest req = TestExecutionListener.mockWebServer.takeRequest();
        Assert.assertEquals(req.getPath(), "/v2.0/compatibility?vin=1234&scope=read_odometer&country=US");
    }

    @Test
    @PrepareForTest(System.class)
    public void testGetCompatibilityWithOptions() throws Exception {
        String vin = "1234";
        String[] scope;
        scope = new String[]{"read_odometer"};

        MockResponse response = new MockResponse()
                .setBody("{ \"compatible\": true }")
                .addHeader("sc-request-id", this.sampleRequestId);
        TestExecutionListener.mockWebServer.enqueue(response);

        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("SMARTCAR_API_ORIGIN")).thenReturn(
                "http://localhost:" + TestExecutionListener.mockWebServer.getPort()
        );
        PowerMockito.when(System.getenv("SMARTCAR_CLIENT_ID")).thenReturn(this.sampleClientId);
        PowerMockito.when(System.getenv("SMARTCAR_CLIENT_SECRET")).thenReturn(this.sampleClientSecret);

        SmartcarCompatibilityRequest request =  new SmartcarCompatibilityRequest.Builder()
                .clientId(this.sampleClientId)
                .clientSecret(this.sampleClientSecret)
                .vin(vin)
                .scope(scope)
                .country("GB")
                .version("1.0")
                .addFlag("foo", "bar")
                .addFlag("test", true)
                .testMode(true)
                .testModeCompatibilityLevel("hello")
                .build();
        Compatibility comp = Smartcar.getCompatibility(request);
        Assert.assertTrue(comp.getCompatible());
        Assert.assertEquals(comp.getMeta().getRequestId(), this.sampleRequestId);
        RecordedRequest req = TestExecutionListener.mockWebServer.takeRequest();
        Assert.assertEquals(req.getPath(), "/v1.0/compatibility?vin=1234&scope=read_odometer&country=GB&flags=foo%3Abar%20test%3Atrue&mode=test&test_mode_compatibility_level=hello");
    }

    @Test
    @PrepareForTest(System.class)
    public void testGetCompatibilitySimulatedMode() throws Exception {
        String vin = "1234";
        String[] scope;
        scope = new String[]{"read_odometer"};

        MockResponse response = new MockResponse()
                .setBody("{ \"compatible\": true }")
                .addHeader("sc-request-id", this.sampleRequestId);
        TestExecutionListener.mockWebServer.enqueue(response);

        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("SMARTCAR_API_ORIGIN")).thenReturn(
                "http://localhost:" + TestExecutionListener.mockWebServer.getPort()
        );
        PowerMockito.when(System.getenv("SMARTCAR_CLIENT_ID")).thenReturn(this.sampleClientId);
        PowerMockito.when(System.getenv("SMARTCAR_CLIENT_SECRET")).thenReturn(this.sampleClientSecret);

        SmartcarCompatibilityRequest request =  new SmartcarCompatibilityRequest.Builder()
                .clientId(this.sampleClientId)
                .clientSecret(this.sampleClientSecret)
                .vin(vin)
                .scope(scope)
                .mode("simulated")
                .build();
        Compatibility comp = Smartcar.getCompatibility(request);
        Assert.assertTrue(comp.getCompatible());
        Assert.assertEquals(comp.getMeta().getRequestId(), this.sampleRequestId);
        RecordedRequest req = TestExecutionListener.mockWebServer.takeRequest();
        Assert.assertEquals(req.getPath(), "/v2.0/compatibility?vin=1234&scope=read_odometer&country=US&mode=simulated");
    }

    @Test
    @PrepareForTest(System.class)
    public void testGetCompatibilityWithoutRequiredOptions() {
        boolean thrown = false;
        try {
            new SmartcarCompatibilityRequest.Builder()
                    .clientId(this.sampleClientId)
                    .clientSecret(null)
                    .build();
        } catch (Exception e) {
            thrown = true;
            Assert.assertEquals(e.getMessage(), "clientSecret must be defined");
        }
        Assert.assertTrue(thrown);

        thrown = false;
        try {
            new SmartcarCompatibilityRequest.Builder()
                    .clientId(null)
                    .clientSecret(this.sampleClientSecret)
                    .build();
        } catch (Exception e) {
            thrown = true;
            Assert.assertEquals(e.getMessage(), "clientId must be defined");
        }
        Assert.assertTrue(thrown);
    }

    @Test
    @PrepareForTest(System.class)
    public void testGetCompatibilityMatrix() {
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("SMARTCAR_API_ORIGIN")).thenReturn(
                "http://localhost:" + TestExecutionListener.mockWebServer.getPort()
        );

        MockResponse response = new MockResponse()
                .setBody("{}")
                .addHeader("sc-request-id", this.sampleRequestId);
        TestExecutionListener.mockWebServer.enqueue(response);

        SmartcarCompatibilityMatrixRequest compatibilityMatrixRequest = new SmartcarCompatibilityMatrixRequest.Builder()
                .clientId(this.sampleClientId)
                .clientSecret(this.sampleClientSecret)
                .build();

        try {
            CompatibilityMatrix matrix = Smartcar.getCompatibilityMatrix(compatibilityMatrixRequest);
            Assert.assertNotNull(matrix);
            Assert.assertEquals(matrix.getResults().size(), 0);
            Assert.assertEquals(matrix.getMeta().getRequestId(), this.sampleRequestId);
        } catch (SmartcarException e) {
            Assert.fail("Exception thrown during getCompatibilityMatrix: " + e.getMessage());
        }
        try {
            TestExecutionListener.mockWebServer.takeRequest();
        } catch (InterruptedException e) {
            Assert.fail("Request was not made to mock server");
        }
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

    @Test
    public void testHashChallenge() throws SmartcarException {
        String hash = Smartcar.hashChallenge("amt", "challenge");
        Assert.assertEquals(hash, "9baf5a7464bd86740ad5a06e439dcf535a075022ed2c92d74efacf646d79328e");
    }

    @Test
    public void testVerifyPayload() throws SmartcarException {
        Assert.assertTrue(
                Smartcar.verifyPayload(
                        "amt",
                        "9baf5a7464bd86740ad5a06e439dcf535a075022ed2c92d74efacf646d79328e",
                        "challenge"
                )
        );
    }
}
