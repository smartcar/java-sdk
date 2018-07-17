package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.testng.Assert.assertNotNull;

/**
 * Integration Test Suite: Authentication
 */
public class AuthTest extends IntegrationTest {
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
    public void testAuthFlowObtainsValidAuthCode() throws Exception {
        Auth authToken = this.getAuth();

        assertNotNull(authToken.getAccessToken());
    }

    /**
     * Tests that a valid user ID can be obtained.
     *
     * @throws SmartcarException if the request fails
     */
    @Test
    public void testGetUserId() throws SmartcarException {
        AuthClient.getUserId(this.accessToken);
    }

    /**
     * Tests that a valid list of vehicles can be obtained.
     *
     * @throws SmartcarException if the request fails
     */
    @Test
    public void testGetVehicleIds() throws SmartcarException {
        AuthClient.getVehicleIds(this.accessToken);
    }
}
