package com.smartcar.sdk;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

/**
 * Integration Test Suite: Authentication
 */
public class AuthTest extends IntegrationTest {
    private final String oemUsername = IntegrationUtils.nonce(16) + "@example.com";
    private final String oemPassword = IntegrationUtils.nonce(16);

    /**
     * Setup tasks to be completed before tests in the suite are executed.
     *
     * @throws MalformedURLException if the WebDriver cannot be initialized with the URL it is given
     */
    @BeforeSuite
    public void beforeSuite() throws MalformedURLException {
        this.startDriver();
    }

    /**
     * Cleanup tasks to be completed after all tests in the suite have completed.
     */
    @AfterSuite
    public void afterSuite() {
        this.stopDriver();
    }

    /**
     * Tests that the OAuth 2.0 flow succeeds and results in a valid auth code.
     *
     * @throws MalformedURLException when a URL cannot be parsed during testing
     */
    @Test
    public void testAuthFlowObtainsValidAuthCode() throws MalformedURLException {
        AuthClient testSubject = new AuthClient(
                this.clientId,
                this.clientSecret,
                this.redirectUri,
                this.scope,
                this.development
        );

        String connectAuthUrl = testSubject.getAuthUrl();
        String mockAuthUrl = null;

        // 1 -- Initiate the OAuth 2.0 flow at https://connect.smartcar.com
        this.driver.get(connectAuthUrl);

        List<WebElement> webOemButtons = this.driver.findElements(By.cssSelector("body > div > a.button"));

        for (WebElement webButton : webOemButtons) {
            if(webButton.getAttribute("href").startsWith("https://mock.smartcar.com")) {
                mockAuthUrl = webButton.getAttribute("href");
                break;
            }
        }

        if(mockAuthUrl == null) {
            fail("Expected OEM auth button was not found.");
            return;
        }

        // 2 -- Find the Mock OEM button, and navigate to the Mock OEM login page.
        this.driver.get(mockAuthUrl);

        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#username")));

        // 3 -- Enter OEM user credentials and submit the form.
        this.driver.findElement(By.cssSelector("input#username")).sendKeys(this.oemUsername);
        this.driver.findElement(By.cssSelector("input#password")).sendKeys(this.oemPassword);
        this.driver.findElement(By.cssSelector("#approval-button")).submit();

        // 4 -- Approve/grant access to the checked vehicles bu submitting the next form.
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".review-permissions")));

        this.driver.findElement(By.cssSelector("#approval-button")).click();

        // 5 -- After the triggered redirect, parse the redirect URL's query string for the auth code.
        URL redirectUrl = new URL(this.driver.getCurrentUrl());
        String[] params = redirectUrl.getQuery().split("&");
        String code = null;

        for(String param : params) {
            String[] currentParam = param.split("=");

            if(currentParam[0].equals("code")) {
                code = currentParam[1];
                break;
            }
        }

        assertNotNull(code);

    }
}
