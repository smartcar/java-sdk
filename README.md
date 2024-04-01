# Smartcar Java SDK [![Build Status][ci-image]][ci-url] [![Code Coverage][coverage-image]][coverage-url] [![JavaDoc][javadoc-image]][javadoc-url] [![Maven Central][maven-image]][maven-url]

## Resources
 * [Smartcar Developer Dashboard][smartcar-developer]
 * [Smartcar API Reference][smartcar-docs-api]
 * [Smartcar Java SDK Documentation][smartcar-sdk-javadoc]

## Installation
The recommended method for obtaining the SDK is via Gradle or Maven through the Maven Central repository. Direct download links are also provided below.

### Gradle
```groovy
compile "com.smartcar.sdk:java-sdk:4.1.0"
```

### Maven
```xml
<dependency>
  <groupId>com.smartcar.sdk</groupId>
  <artifactId>java-sdk</artifactId>
  <version>4.1.0</version>
</dependency>
```

### Jar Direct Download
* [java-sdk-4.1.0.jar](https://repo1.maven.org/maven2/com/smartcar/sdk/java-sdk/4.1.0/java-sdk-4.1.0.jar)
* [java-sdk-4.1.0-sources.jar](https://repo1.maven.org/maven2/com/smartcar/sdk/java-sdk/4.1.0/java-sdk-4.1.0-sources.jar)
* [java-sdk-4.1.0-javadoc.jar](https://repo1.maven.org/maven2/com/smartcar/sdk/java-sdk/4.1.0/java-sdk-4.1.0-javadoc.jar)

Signatures and other downloads available at [Maven Central](https://central.sonatype.com/artifact/com.smartcar.sdk/java-sdk/4.1.0).

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
    String mode = "test";

    // Initialize a new AuthClient with your credentials.
    AuthClient authClient = new AuthClient.Builder
        .clientId(clientId)
        .clientSecret(clientSecret)
        .redirectUri(redirectUri)
        .mode(mode);

    // Retrieve the auth URL to start the OAuth flow.
    String authUrl = authClient.authUrlBuilder(scope)
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
    VehicleIds response = AuthClient.getVehicleIds(auth.getAccessToken());
    String[] vehicleIds = response.getVehicleIds();
    ```

2.  Create an instance of `Vehicle`:

    ```java
    Vehicle vehicle = new Vehicle(vehicleIds[0], auth.getAccessToken());
    ```

3.  You can now access all information about the specified vehicle:

    ```java
    // Retrieve the vehicle's VIN
    String vin = vehicle.vin().getVin();

    // Read the vehicle's odometer
    VehicleOdometer odometerData = vehicle.odometer();
    double odometer = odometerData.getDistance();

    // Retrieve the vehicle's location
    VehicleLocation locationData = vehicle.location();
    String latLong = locationData.getLatitude() + ", " + locationData.getLongitude();

    // Lock and unlock the vehicle
    vehicle.lock();
    vehicle.unlock();
    ```

## Supported Java Releases
Smartcar aims to support the SDK on all LTS Java releases (and Java 8) until the "Extended Support" date as defined in the Oracle Java SE Support Roadmap

In accordance with the Semantic Versioning specification, the addition of support for new Java releases would result in a MINOR version bump and the removal of support for Java releases would result in a MAJOR version bump.

[1]: https://tools.ietf.org/html/rfc6749#section-1.3.1
[2]: https://tools.ietf.org/html/rfc6749

[smartcar-developer]: https://developer.smartcar.com
[smartcar-docs-api]: https://smartcar.com/docs
[smartcar-sdk-javadoc]: https://smartcar.github.io/java-sdk

[ci-image]: https://travis-ci.com/smartcar/java-sdk.svg?token=jMbuVtXPGeJMPdsn7RQ5&branch=master
[ci-url]: https://travis-ci.com/smartcar/java-sdk
[coverage-image]: https://codecov.io/gh/smartcar/java-sdk/branch/master/graph/badge.svg?token=nZAITx7w3X
[coverage-url]: https://codecov.io/gh/smartcar/java-sdk
[javadoc-image]: https://img.shields.io/badge/javadoc-4.1.0-brightgreen.svg
[javadoc-url]: https://smartcar.github.io/java-sdk
[maven-image]: https://img.shields.io/maven-central/v/com.smartcar.sdk/java-sdk.svg?label=Maven%20Central
[maven-url]: https://central.sonatype.com/artifact/com.smartcar.sdk/java-sdk
