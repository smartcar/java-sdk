package com.smartcar.sdk.helpers;

import com.smartcar.sdk.AuthClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

/** Provides all shared functionality among integration tests. */
public class AuthHelpers {
  public static final String[] DEFAULT_SCOPE = {
    "required:read_vehicle_info",
    "required:read_location",
    "required:read_odometer",
    "required:control_security",
    "required:read_vin",
    "required:read_fuel",
    "required:read_battery",
    "required:read_charge",
    "required:read_engine_oil",
    "required:read_tires",
  };

  private static final boolean HEADLESS =
      System.getenv("CI") != null || System.getenv("HEADLESS") != null;
  private static final HashMap<String, String> ENV_VAR_CACHE = new HashMap<>();

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

  /**
   * Creates an AuthClient builder and sets Client ID, Client Secret, and Redirect URI and also
   * enables test mode.
   */
  public static AuthClient.Builder getConfiguredAuthClientBuilder() {
    return new AuthClient.Builder()
        .clientId(getClientId())
        .clientSecret(getClientSecret())
        .redirectUri("https://example.com/auth")
        .testMode(true);
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
   * @param make - which make to select
   * @return auth code
   */
  public static String runAuthFlow(String authorizeURL, String make) {
    FirefoxOptions options = new FirefoxOptions().setHeadless(HEADLESS);

    WebDriver driver = new FirefoxDriver(options);
    WebDriverWait wait = new WebDriverWait(driver, 10);

    driver.get(authorizeURL);

    // Preamble
    WebElement preambleButton =
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("continue-button")));
    preambleButton.click();

    // Brand Selector
    WebElement brandButton =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button.brand-selector-button[data-make=" + make + "]")));
    brandButton.click();

    // Login
    String email = UUID.randomUUID() + "@email.com";
    WebElement signInButton =
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sign-in-button")));
    driver.findElement(By.id("username")).sendKeys(email);
    driver.findElement(By.id("password")).sendKeys("password");
    signInButton.click();

    // Grant
    WebElement permissionsApprovalButton =
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("approval-button")));
    permissionsApprovalButton.click();

    WebElement continueButton =
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("continue-button")));
    continueButton.click();

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
   * Wrapper around System.getenv that throws an error if the variable is not set (also caches the
   * value)
   *
   * @return the environment variable that the the name maps to
   */
  private static String safeGetEnv(String name) {
    if (ENV_VAR_CACHE.containsKey(name)) {
      return ENV_VAR_CACHE.get(name);
    }

    String value = System.getenv(name);
    if (value == null) {
      throw new RuntimeException("\"" + name + "\" environment variable must be set");
    }
    ENV_VAR_CACHE.put(name, value);
    return value;
  }
}
