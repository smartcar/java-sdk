package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.testng.Assert.assertNotNull;

/**
 * Integration Test Suite: Authentication
 */
public class AuthTest extends IntegrationTest {
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
}
