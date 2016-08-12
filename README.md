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
