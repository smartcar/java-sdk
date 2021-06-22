package com.smartcar.sdk;

import com.smartcar.sdk.data.Auth;
import com.smartcar.sdk.data.SmartcarResponse;
import com.smartcar.sdk.data.VehicleIds;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Provides all shared functionality among integration tests. */
abstract class IntegrationTest {
  private static final int WEBDRIVER_DEFAULT_TIMEOUT_SECONDS = 10;
  private static Auth auth;

  final String authOemUsername = IntegrationUtils.nonce(16) + "@smartcar.com";
  final String authOemPassword = IntegrationUtils.nonce(16);

  AuthClient authClient;
  final String authClientId = System.getenv("SMARTCAR_CLIENT_ID");
  final String authClientSecret = System.getenv("SMARTCAR_CLIENT_SECRET");
  final String authRedirectUri = System.getenv("SMARTCAR_REDIRECT_URI");
  final boolean authDevelopment = true;
  final String[] authScope = {
    "control_security:lock",
    "control_security:unlock",
    "read_vehicle_info",
    "read_vin",
    "read_location",
    "read_odometer",
    "read_fuel",
    "read_tires",
    "read_engine_oil",
    "read_battery",
    "read_charge",
    "control_charge"
  };
  final String authMake = "CHEVROLET";

  WebDriver driver;
  WebDriverWait wait;

  /**
   * Maintains the auth singleton that provides auth credentials for integration tests.
   *
   * @return the current auth credentials
   */
  Auth getAuth() throws Exception {
    if (IntegrationTest.auth == null) {
      IntegrationTest.auth = this.getAuth(this.authMake);
    }

    return IntegrationTest.auth;
  }

  /**
   * Retrieves auth credentials for a specified make. This method does not maintain the auth
   * singleton and returns a new auth credential every time it is invoked.
   *
   * @param make the make to be selected within the auth flow
   * @return a new set of auth credentials
   */
  Auth getAuth(String make) throws Exception {
    this.authClient = new AuthClient.Builder()
            .clientId(this.authClientId)
            .clientSecret(this.authClientSecret)
            .redirectUri(this.authRedirectUri)
            .testMode(true)
            .build();

    String authUrl = this.authClient.new AuthUrlBuilder(this.authScope).build();
    String authCode = this.getAuthCode(authUrl, this.authOemUsername, this.authOemPassword, make);

    Auth auth = authClient.exchangeCode(authCode);

    return auth;
  }

  /**
   * Obtain a single vehicle of the specified make with an authorization independent of the auth
   * singleton created using `getAuth`
   *
   * @param make the make to be selected within the auth flow
   * @return an authorized vehicle
   */
  Vehicle getVehicle(String make) throws Exception {
    Auth auth = this.getAuth(make);
    String accessToken = auth.getAccessToken();

    VehicleIds vehicles = Smartcar.getVehicles(accessToken);
    String[] vehicleIds = vehicles.getVehicleIds();

    Vehicle vehicle = new Vehicle(vehicleIds[0], accessToken);

    return vehicle;
  }

  /**
   * Retrieves an auth code that can be exchanged for an auth token.
   *
   * @param connectAuthUrl the Smartcar auth URL
   * @param oemUsername the OEM username
   * @param oemPassword the OEM password
   * @return the resulting auth code
   * @throws Exception if the auth code could not be obtained
   */
  private String getAuthCode(
      String connectAuthUrl, String oemUsername, String oemPassword, String make) throws Exception {
    this.startDriver();

    // 1 -- Initiate the OAuth 2.0 flow at https://connect.smartcar.com
    this.driver.get(connectAuthUrl);
    // Click continue on preamble screen.
    this.driver.findElement(By.id("continue-button")).click();

    // 2 -- Find the Mock OEM button, and navigate to the Mock OEM login page.
    this.driver
        .findElement(
            By.xpath("//button[@data-make='" + make + "' and @class='brand-selector-button']"))
        .click();

    this.wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#username")));

    // 3 -- Enter OEM user credentials and submit the form.
    this.driver.findElement(By.id("username")).sendKeys(oemUsername);
    this.driver.findElement(By.id("password")).sendKeys(oemPassword);
    this.driver.findElement(By.id("sign-in-button")).submit();

    // 4 -- Approve/grant access to the checked vehicles bu submitting the next form.
    this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-content")));

    // Select Chevy Volt by unclicking all the boxes except the Volt
    // Select Tesla Model S by unclicking all the boxes except the Model S
    // This is needed because the Volt and Model S cover all the endpoints that are needed.
    List<WebElement> elements = this.driver.findElements(By.className("input-button-label"));
    for (WebElement el : elements) {
      WebElement vehicleText = el.findElement(By.className("input-button-label-text-bordered"));
      WebElement checkbox = el.findElement(By.className("input-button-custom"));
      if (vehicleText == null) {
        throw new Exception("input-button-label-text-bordered not found");
      } else if (checkbox == null) {
        throw new Exception("input-button-custom not found");
      } else if (!(vehicleText.getText().contains("Volt")
          || vehicleText.getText().contains("Model S"))) {
        checkbox.click();
      }
    }

    this.driver.findElement(By.id("approval-button")).click();

    // 5 -- After the triggered redirect, parse the redirect URL's query string for the auth code.
    URL redirectUrl = new URL(this.driver.getCurrentUrl());
    String[] params = redirectUrl.getQuery().split("&");
    String code = null;

    for (String param : params) {
      String[] currentParam = param.split("=");

      if (currentParam[0].equals("code")) {
        code = currentParam[1];
        break;
      }
    }

    this.stopDriver();

    if (code == null) {
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
    if (System.getProperty("selenium.debug") != null) {
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

    this.driver =
        new RemoteWebDriver(
            new URL("http://" + seleniumHost + ":" + seleniumPort + "/wd/hub"),
            desiredCapabilities);
  }

  /**
   * Starts a local ChromeDriver for easier debugging of Selenium tests. Use -Dselenium.debug=true
   * for debugging
   */
  private void startLocalDriver() {
    this.driver = new ChromeDriver();
  }

  /** Stops the Selenium WebDriver. */
  void stopDriver() {
    this.driver.quit();
  }
}
