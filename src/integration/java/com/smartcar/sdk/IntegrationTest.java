package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Provides all shared functionality among integration tests.
 */
abstract class IntegrationTest {
    private static final int WEBDRIVER_DEFAULT_TIMEOUT_SECONDS = 10;
    private static Auth auth;

    final String authOemUsername = IntegrationUtils.nonce(16) + "@smartcar.com";
    final String authOemPassword = IntegrationUtils.nonce(16);

    AuthClient authClient;
    final String authClientId = System.getenv("INTEGRATION_CLIENT_ID");
    final String authClientSecret = System.getenv("INTEGRATION_CLIENT_SECRET");
    final String authRedirectUri = "https://example.com/auth";
    final boolean authDevelopment = true;
    final String[] authScope = {
            "control_security:lock",
            "control_security:unlock",
            "read_vehicle_info",
            "read_vin",
            "read_location",
            "read_odometer",
            "read_fuel",
            "read_battery",
            "read_charge"
    };

    WebDriver driver;
    WebDriverWait wait;

    /**
     * Maintains the auth singleton that provides auth credentials for integration tests.
     *
     * @return the current auth credentials
     */
    Auth getAuth() throws Exception {
        if(IntegrationTest.auth == null) {
            this.authClient = new AuthClient(
                    this.authClientId,
                    this.authClientSecret,
                    this.authRedirectUri,
                    this.authScope,
                    this.authDevelopment
            );
            String authUrl = this.authClient.new AuthUrlBuilder().build();

            String authCode = this.getAuthCode(authUrl, this.authOemUsername, this.authOemPassword);

            IntegrationTest.auth = authClient.exchangeCode(authCode);
        }

        return IntegrationTest.auth;
    }

    /**
     * Retrieves an auth code that can be exchanged for an auth token.
     *
     * @param connectAuthUrl the Smartcar auth URL
     * @param oemUsername the OEM username
     * @param oemPassword the OEM password
     *
     * @return the resulting auth code
     * @throws Exception if the auth code could not be obtained
     */
    private String getAuthCode(String connectAuthUrl, String oemUsername, String oemPassword) throws Exception {
        this.startDriver();

        // 1 -- Initiate the OAuth 2.0 flow at https://connect.smartcar.com
        this.driver.get(connectAuthUrl);

        WebElement teslaButton = this.driver.findElement(By.cssSelector("button[data-make='AUDI']"));

        // 2 -- Find the Mock OEM button, and navigate to the Mock OEM login page.
        teslaButton.click();

        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#username")));

        // 3 -- Enter OEM user credentials and submit the form.
        this.driver.findElement(By.cssSelector("input#username")).sendKeys(oemUsername);
        this.driver.findElement(By.cssSelector("input#password")).sendKeys(oemPassword);
        this.driver.findElement(By.cssSelector("#sign-in-button")).submit();

        // 4 -- Approve/grant access to the checked vehicles bu submitting the next form.
        this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-content")));

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

        this.stopDriver();

        if(code == null) {
            throw new Exception("Failed obtaining auth code.");
        }

        return code;
    }

    /**
     * Starts the Selenium WebDriver.
     *
     * @throws MalformedURLException if the WebDriver cannot be initialized with the URL it is given
     */
    void startDriver() throws MalformedURLException {
        if(System.getProperty("selenium.debug") != null) {
            this.startLocalDriver();
        } else {
            this.startRemoteDriver();
        }

        this.wait = new WebDriverWait(this.driver, IntegrationTest.WEBDRIVER_DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Starts the remote WebDriver in the configured docker container.
     *
     * @throws MalformedURLException if the WebDriver cannot be initialized with the URL it is given
     */
    private void startRemoteDriver() throws MalformedURLException {
        String seleniumBrowser = System.getProperty("selenium.browser");
        String seleniumHost = System.getProperty("selenium.host");
        String seleniumPort = System.getProperty("selenium.port");

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName(seleniumBrowser);

        this.driver = new RemoteWebDriver(new URL("http://" + seleniumHost + ":" + seleniumPort + "/wd/hub"), desiredCapabilities);
    }

    /**
     * Starts a local ChromeDriver for easier debugging of Selenium tests.
     */
    private void startLocalDriver() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        this.driver = new ChromeDriver();
    }

    /**
     * Stops the Selenium WebDriver.
     */
    void stopDriver() {
        this.driver.quit();
    }
}
