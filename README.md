# Smartcar Java SDK

Get started at https://developer.smartcar.com

## Installation

### Maven

```xml
<dependency>
  <groupId>com.smartcar</groupId>
  <artifactId>sdk</artifactId>
  <version>0.0.1</version>
</dependency>
```

### Gradle

```groovy
compile "com.smartcar:sdk:0.0.1"
```

## Overall Flow

* Create a new `Client` object with your `clientId`, `clientSecret`,
`redirectUri`, and `scope`

```java
static String ID = "YOUR_CLIENT_ID";
static String SECRET = "YOUR_CLIENT_SECRET";
static String REDIRECT_URI = "http://localhost:5000/callback";
static String[] scope = { "read_vehicle_info", "read_location", "read_engine"};

static Smartcar client = new Smartcar(ID, SECRET, REDIRECT_URI, scope);
```

* Redirect the user to an OEM login page with `getAuthUrl`

```java
String url = client.getAuthUrl("bmw").toString();
```

* The user will login, and then accept or deny your `scope`'s permissions
* Handle the get request to `redirectUri`
  * If the user accepted your permissions, the request's query parameters will contain an
    authentication code keyed by `code`
    * Use `exchangeCode` with this code to obtain an access object
    containing an access token (lasting 2 hours) and a refresh token
    (lasting 60 days)
      * save this access object
    * If the user denied your permissions, the request's query parameters will contain an
    error keyed by `error`
    * If you passed a state parameter to `getAuthUrl`, the request's query parameters will contain an
    the passed state keyed by `state`

```java
// following code uses SparkJava framework, syntax may differ
get("/callback", (req, res) -> {
    String code = req.queryMap().get("code").value();
    Access access = client.exchangeCode(code);
    // save access for use
```

* Redirect to your main app endpoint
* Handle the get request to your main app endpoint
* When necessary, use `exchangeToken` on your saved access object to automatically refresh an
expired `access_token`

```java
if (oldAccess.expired()) {
  Access newRefreshedAccess = client.exchangeToken(oldAccess.getRefreshToken());
  // newRefreshedAccess.getAccessToken() now holds a new access token
}
```

* Get the ids of the users vehicles with `getVehicles`

```java
Api.Vehicles vehicles = client.getVehicles(access.getAccessToken());
String[] vehicleIds = vehicles.vehicles;
```

* Create a new `Vehicle` object using a vehicleId from the previous response, and
the `access_token`

```java
String vehicleId = vehicleIds[0];
Vehicle firstVehicle = new Vehicle(vehicleId, access.getAccessToken());
```

* Do stuff with the vehicle data!

```java
// latitude and longitude!
Api.Location location = vehicle.location();
```

## Full Example

See the full example here (no link yet, example is not in repo yet)

## Structure

### Access:
* Represents the JSON object returned by the Smartcar Authentication API when
  exchanging a refresh token for a new access token, or when exchanging a
  code for an access token.
* `Smartcar.exchangeCode` and `Smartcar.exchangeToken` use `Access` to convert the
  JSON string returned by `AccessRequest.code` and `AccessRequest.token`.

### AccessRequest:
* Represents a request to the Smartcar Authentication API.
* Smartcar.exchangeCode uses AccessRequest.code, and Smartcar.exchangeToken
  uses AccessRequest.token.

### Api:
* Contains classes that represent the JSON objects that are send to, and
  recieved from, the Smartcar Vehicle API.
* The action of requesting vehicle information is a Get Intent.
* The action of telling the vehicle to do something is an Action Intent.
* Most classes in Api represent the JSON objects recieved from the API after
  a Get Intent. These include `Accelerometer`, `Compass`, and `DriveMode`
* Classes that end in `Action` such as `GenericAction` or
  `ChargeScheduleAction` represent the JSON objects that are *sent* to the
  Smartcar Vehicle API after an Action Intent.
* The `Window` and `Mirror` classes represent data that can be both send
  *and* recieved from the Smartcar Vehicle API.
* Classes that have a plural and singular form represent data that is
  send and recieved in arrays. These include `Airbags/Airbag`, `Doors/Door`,
  and `SafetyLocks/SafetyLock`. The plural class represents the API response
  as a whole, and the singular class represents an individual object in the
  response array.

### Exceptions:
* Contains exceptions that are thrown by `Util.call`.

### Smartcar:
* Contains the methods needed by a developer to interact with the Smartcar
  Platform.

### Util:
* Contains Util.call and Util.join. Util.call sends requests from
  `VehicleRequest` and `AccessRequest`, returns their response bodies, and
  throws exceptions whenever a request results in a non 200 response from the
  Vehicle API.

### Vehicle:
* Represents a user's vehicle. A `Vehicle`'s methods can ask the vehicle for
  data and tell the vehicle to perform actions.
* If the request for vehicle data succeeds, the JSON object recieved is
  converted into an `Api` object.

### VehicleRequest:
* Represents a request ot the Smartcar Vehcicle API.
* Most data requesting methods in `Vehicle` use `VehicleRequest.get`, and all
  action performing methods in `Vehicle` use `VehicleRequest.action`.
* `VehicleRequest.vehicles` is used by `Smartcar.getVehicles`.
