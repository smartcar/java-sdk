package com.smartcar.sdk.helpers;

import com.smartcar.sdk.AuthClient;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

/** Provides all shared functionality among integration tests. */
public class AuthHelpers {
  public static final String[] DEFAULT_SCOPE = {
      "required:control_navigation",
      "required:control_security",
      "required:read_vehicle_info",
      "required:read_location",
      "required:read_odometer",
      "required:read_vin",
      "required:read_fuel",
      "required:read_battery",
      "required:read_charge",
      "required:read_engine_oil",
      "required:read_tires",
      "required:read_diagnostics"
  };

  private static final boolean HEADLESS = System.getenv("CI") != null || System.getenv("HEADLESS") != null;
  private static final HashMap<String, String> ENV_VAR_CACHE = new HashMap<>();

  public static String getSeleniumRemoteUrl() {
    return safeGetEnv("SELENIUM_REMOTE_URL");
  }

  public static String getClientId() {
    return safeGetEnv("E2E_SMARTCAR_CLIENT_ID");
  }

  public static String getClientSecret() {
    return safeGetEnv("E2E_SMARTCAR_CLIENT_SECRET");
  }

  public static String getApplicationManagementToken() {
    return safeGetEnv("E2E_SMARTCAR_AMT");
  }

  public static String getWebhookId() {
    return safeGetEnv("E2E_SMARTCAR_WEBHOOK_ID");
  }

  public static String getBrowser() {
    return safeGetEnv("BROWSER", "firefox");
  }

  public static WebDriver setupDriver() {
    String browser = getBrowser();
    WebDriver driver;

    if (getSeleniumRemoteUrl() != null) {
      Capabilities capabilities = "chrome".equalsIgnoreCase(browser)
        ? new ChromeOptions()
        : new FirefoxOptions();
      try {
        driver = new RemoteWebDriver(
            java.net.URI.create(getSeleniumRemoteUrl()).toURL(),
            capabilities
        );
      } catch (MalformedURLException e) {
        throw new RuntimeException("Invalid SELENIUM_REMOTE_URL", e);
      }
      return driver;
    }

    if ("chrome".equalsIgnoreCase(browser)) {
      ChromeOptions options = new ChromeOptions();
      if (HEADLESS) {
        options.addArguments("--headless");
      }
      driver = new ChromeDriver(options);
    } else {
      // Default to Firefox
      FirefoxOptions options = new FirefoxOptions();
      if (HEADLESS) {
        options.addArguments("--headless");
      }

      // Set Firefox binary path if available (for CI environments)
      String firefoxPath = System.getenv("FIREFOX_BINARY_PATH");
      if (firefoxPath == null) {
        // Try common CI locations
        String homeDir = System.getProperty("user.home");
        String[] candidatePaths = {
          homeDir + "/firefox-latest/firefox/firefox",
          "/usr/bin/firefox",
          "/usr/lib/firefox/firefox"
        };

        for (String path : candidatePaths) {
          if (new File(path).exists()) {
            firefoxPath = path;
            break;
          }
        }
      }
      if (firefoxPath != null) {
        System.out.println("Using Firefox binary: " + firefoxPath);
        options.setBinary(firefoxPath);
      }

      driver = new FirefoxDriver(options);
    }

    return driver;
  }

  /**
   * Creates an AuthClient builder and sets Client ID, Client Secret, and Redirect
   * URI and also
   * enables test mode.
   */
  public static AuthClient.Builder getConfiguredAuthClientBuilder() throws Exception {
    return new AuthClient.Builder()
        .clientId(getClientId())
        .clientSecret(getClientSecret())
        .redirectUri("https://example.com/auth")
        .mode("test");
  }

  public static String runAuthFlow(String authorizeURL) {
    return runAuthFlow(authorizeURL, "CHEVROLET");
  }

  /**
   * Retrieves an auth code that can be exchanged for an auth token.
   *
   * @throws IllegalStateException if URL is invalid
   * @throws IllegalStateException if error is returned from connect
   * @param authorizeURL - fully built authorize url
   * @param make         - which make to select
   * @return auth code
   */
  public static String runAuthFlow(String authorizeURL, String make) {

    WebDriver driver = setupDriver();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    driver.get(authorizeURL);

    // Preamble
    WebElement preambleButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("continue-button")));
    preambleButton.click();

    // Brand Selector
    WebElement brandButton = wait.until(
        ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("button#" + make.toUpperCase() + ".brand-list-item")));
    brandButton.click();

    // Login
    String email = UUID.randomUUID() + "@email.com";
    WebElement signInButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sign-in-button")));
    driver.findElement(By.id("username")).sendKeys(email);
    driver.findElement(By.id("password")).sendKeys("password");
    signInButton.click();

    // Grant
    WebElement permissionsApprovalButton = wait
        .until(ExpectedConditions.presenceOfElementLocated(By.id("approval-button")));
    permissionsApprovalButton.click();

    // Redirect
    wait.until(ExpectedConditions.urlContains("https://example.com"));

    URL url = null;
    try {
      url = new URL(driver.getCurrentUrl());
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Failed to parse the redirect URL", e);
    }
    driver.quit();
    return getCodeFromUrl(url);
  }

  private static String getCodeFromUrl(URL url) {
    HashMap<String, String> searchParams = new HashMap<>();

    for (String param : url.getQuery().split("&")) {
      String[] parts = param.split("=");
      searchParams.put(parts[0], parts[1]);
    }

    if (!searchParams.containsKey("code")) {
      throw new IllegalStateException(
          "Did not get code in url! Error message: " + searchParams.get("error"));
    }

    return searchParams.get("code");
  }

  /**
   * Wrapper around System.getenv that returns a default value if the variable is
   * not set
   * (also caches the value).
   *
   * @param name         The name of the environment variable.
   * @param defaultValue The default value to return if the environment variable
   *                     is not set.
   * @return The environment variable value or the default value if the variable
   *         is not set.
   */
  private static String safeGetEnv(String name, String defaultValue) {
    if (ENV_VAR_CACHE.containsKey(name)) {
      return ENV_VAR_CACHE.get(name);
    }

    String value = System.getenv(name);
    if (value == null) {
      value = defaultValue; // Use default value if environment variable is not set
    }
    ENV_VAR_CACHE.put(name, value);
    return value;
  }

  // Optionally, you can also maintain the original method signature as an
  // overload
  private static String safeGetEnv(String name) {
    return safeGetEnv(name, null);
  }
}
