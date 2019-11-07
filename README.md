# Smartcar Java SDK [![Build Status][ci-image]][ci-url] [![Code Coverage][coverage-image]][coverage-url] [![JavaDoc][javadoc-image]][javadoc-url]

## Resources
 * [Smartcar Developer Dashboard][smartcar-developer]
 * [Smartcar API Reference][smartcar-docs-api]
 * [Smartcar Java SDK Documentation][smartcar-sdk-javadoc]

## Installation
The recommended method for obtaining the SDK is via Gradle or Maven. Direct
download links are also provided below.

### Gradle
```groovy
compile "com.smartcar.sdk:java-sdk:2.3.0"
```

### Maven
```xml
<dependency>
  <groupId>com.smartcar.sdk</groupId>
  <artifactId>java-sdk</artifactId>
  <version>2.3.0</version>
</dependency>
```

### Jar Direct Download
* [java-sdk-2.3.0.jar](https://bintray.com/smartcar/library/download_file?file_path=com%2Fsmartcar%2Fsdk%2Fjava-sdk%2F2.3.0%2Fjava-sdk-2.3.0.jar)
* [java-sdk-2.3.0-sources.jar](https://bintray.com/smartcar/library/download_file?file_path=com%2Fsmartcar%2Fsdk%2Fjava-sdk%2F2.3.0%2Fjava-sdk-2.3.0-sources.jar)
* [java-sdk-2.3.0-docs.jar](https://bintray.com/smartcar/library/download_file?file_path=com%2Fsmartcar%2Fsdk%2Fjava-sdk%2F2.3.0%2Fjava-sdk-2.3.0-docs.jar)


## Usage

### Authentication
For authentication, Smartcar uses the [authorization code request][1] flow of
the [OAuth 2.0 specification][2]. Before you can make successful calls to the
Smartcar platform, you will need to authenticate with Smartcar, and then obtain
a valid access token for the target vehicle.



1.  Make sure you have your application set up in the
    [Smartcar Developer Dashboard][smartcar-developer]. You will need the following 3 pieces of
    information associated with your application:
    * Client ID
    * Client Secret
    * Redirect URI
2.  You can then generate an authentication URL for your user:

    ```java
    // Setup
    String clientId = "";
    String clientSecret = "";
    String redirectUri = "";
    String[] scope = {};
    boolean development = true;

    // Initialize a new AuthClient with your credentials.
    AuthClient authClient = new AuthClient(clientId, clientSecret, redirectUri, scope, development);

    // Retrieve the auth URL to start the OAuth flow.
    String authUrl = authClient.authUrlBuilder()
            .setApprovalPrompt(true)
            .setState("some state")
            .build();
    ```

3.  Allow the user to complete their portion of the OAuth flow using the
    generated URL.

4.  Once the user is sent back to the redirect url, the required
    authorization code will be included in the query string:

    `https://redirect-url.example.com/?code=<AUTHORIZATION_CODE>`

5.  Given the authorization code, you can now exchange it for an authorization
    token which can be used to access the Smartcar platform:

    ```java
    Auth auth = authClient.exchangeCode(code);
    ```

### Vehicle Data & Commands
Now that you have authenticated and can access the Smartcar platform, you can
start making requests to vehicles.

1.  Obtain a list of authorized vehicles:

    ```java
    SmartcarResponse<VehicleIds> vehicleIdsResponse = AuthClient.getVehicleIds(auth.getAccessToken());
    String[] vehicleIds = vehicleIdsResponse.getData().getVehicleIds();
    ```

2.  Create an instance of `Vehicle`:

    ```java
    Vehicle vehicle = new Vehicle(vehicleIds[0], auth.getAccessToken());
    ```

3.  You can now access all information about the specified vehicle:

    ```java
    // Retrieve the vehicle's VIN
    String vin = vehicle.vin();

    // Read the vehicle's odometer
    SmartcarResponse<VehicleOdometer> odometerResponse = vehicle.odometer();
    VehicleOdometer odometerData = odometerResponse.getData();
    double odometer = odometerData.getDistance();

    // Retrieve the vehicle's location
    SmartcarResponse<VehicleLocation> locationResponse = vehicle.location();
    VehicleLocation locationData = locationResponse.getData();
    String latLong = locationData.getLatitude() + ", " + locationData.getLongitude();

    // Lock and unlock the vehicle
    vehicle.lock();
    vehicle.unlock();
    ```

[1]: https://tools.ietf.org/html/rfc6749#section-1.3.1
[2]: https://tools.ietf.org/html/rfc6749

[smartcar-developer]: https://developer.smartcar.com
[smartcar-docs-api]: https://smartcar.com/docs
[smartcar-sdk-javadoc]: https://smartcar.github.io/java-sdk

[ci-image]: https://travis-ci.com/smartcar/java-sdk.svg?token=jMbuVtXPGeJMPdsn7RQ5&branch=master
[ci-url]: https://travis-ci.com/smartcar/java-sdk
[coverage-image]: https://codecov.io/gh/smartcar/java-sdk/branch/master/graph/badge.svg?token=nZAITx7w3X
[coverage-url]: https://codecov.io/gh/smartcar/java-sdk
[javadoc-image]: https://img.shields.io/badge/javadoc-2.3.0-brightgreen.svg
[javadoc-url]: https://smartcar.github.io/java-sdk
