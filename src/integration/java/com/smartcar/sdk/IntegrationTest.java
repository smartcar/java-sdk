package com.smartcar.sdk;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides all shared functionality among integration tests.
 */
abstract class IntegrationTest {
    private static final int WEBDRIVER_DEFAULT_TIMEOUT_SECONDS = 10;

    final String clientId = System.getenv("INTEGRATION_CLIENT_ID");
    final String clientSecret = System.getenv("INTEGRATION_CLIENT_SECRET");
    final String redirectUri = "https://example.com/auth";
    final String[] scope = {"read_vehicle_info", "read_location", "read_odometer"};
    final boolean development = true;

    WebDriver driver;
    WebDriverWait wait;

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
