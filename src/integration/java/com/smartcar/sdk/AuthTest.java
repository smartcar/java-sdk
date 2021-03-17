package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import com.smartcar.sdk.data.SmartcarResponse;
import com.smartcar.sdk.data.VehicleIds;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

/**
 * Integration Test Suite: Authentication
 */
public class AuthTest extends IntegrationTest {
    private static final String PATTERN_UUID = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

    private String accessToken;

    /**
     * Obtains a valid access token to be used with tests in this suite.
     *
     * @throws Exception if a valid access token cannot be obtained
     */
    @BeforeSuite
    public void beforeSuite() throws Exception {
        Auth auth = this.getAuth();
        this.accessToken = auth.getAccessToken();
    }

    /**
     * Tests that the OAuth 2.0 flow succeeds and results in a valid auth code.
     *
     * @throws MalformedURLException when a URL cannot be parsed during testing
     * @throws Exception when an auth code cannot be obtained
     */
    @Test
    public void testAuthFlowObtainsValidAuthTokens() throws Exception {
        Auth authToken = this.getAuth();

        assertNotNull(authToken.getAccessToken());
        assertNotNull(authToken.getRefreshToken());
        assertNotNull(authToken.getExpiration());
        assertNotNull(authToken.getRefreshExpiration());
    }

    /**
     * Tests that a valid refresh token can be used to obtain new credentials.
     *
     * @throws Exception if new credentials cannot be obtained
     */
    @Test
    public void testRefreshToken() throws Exception {
        Auth oldAuth = this.getAuth();
        Auth newAuth = this.authClient.exchangeRefreshToken(oldAuth.getRefreshToken());

        assertNotNull(newAuth.getAccessToken());
        assertNotNull(newAuth.getRefreshToken());
        assertNotNull(newAuth.getExpiration());
        assertNotNull(newAuth.getRefreshExpiration());
    }

    /**
     * Test that compatibility is returned given scope
     *
     * @throws Exception if compatibility cannot be obtained
     */
    @Test
    public void testIsCompatible() throws Exception {

      String teslaVin = "5YJXCDE22HF068739";
      String audiVin = "WAUAFAFL1GN014882";
      String[] scope = new String[]{"read_location", "read_odometer"}; 

      boolean teslaComp = this.authClient.isCompatible(teslaVin, scope);
      boolean audiComp = this.authClient.isCompatible(audiVin, scope);

      assertTrue(teslaComp);
      assertFalse(audiComp);
    }

    /**
     * Test that compatibility is returned given scope and country
     *
     * @throws Exception if compatibility cannot be obtained
     */
    @Test
    public void testIsCompatibleWithCountry() throws Exception {

      String teslaVin = "5YJXCDE22HF068739";
      String audiVin = "WAUAFAFL1GN014882";
      String[] scope = new String[]{"read_location", "read_odometer"}; 
      String country = "US";

      boolean teslaComp = this.authClient.isCompatible(teslaVin, scope, country);
      boolean audiComp = this.authClient.isCompatible(audiVin, scope, country);

      assertTrue(teslaComp);
      assertFalse(audiComp);
    }

    /**
     * Tests that a valid user ID can be obtained.
     *
     * @throws SmartcarException if the request fails
     */
    @Test
    public void testGetUserId() throws SmartcarException, SmartcarExceptionV2 {
        String userId = AuthClient.getUserId(this.accessToken);

        assertNotNull(userId);
        assertNotEquals(userId.length(), 0);
        assertTrue(userId.matches(AuthTest.PATTERN_UUID));
    }

    /**
     * Tests that a valid list of vehicles can be obtained.
     *
     * @throws SmartcarException if the request fails
     */
    @Test
    public void testGetVehicleIds() throws SmartcarException, SmartcarExceptionV2 {
        SmartcarResponse<VehicleIds> vehicleIdsResponse = AuthClient.getVehicleIds(this.accessToken);
        VehicleIds vehicleIdsData = vehicleIdsResponse.getData();
        String[] vehicleIds = vehicleIdsData.getVehicleIds();

        assertNotNull(vehicleIds);
        assertNotEquals(vehicleIds.length, 0);
        assertTrue(vehicleIds[0].matches(AuthTest.PATTERN_UUID));
    }
}
