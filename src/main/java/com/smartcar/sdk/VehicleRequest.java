package com.smartcar.sdk;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.HttpUrl;

class VehicleRequest {

  private final String accessToken;
  private String vehicleId, endpoint;
  private HttpUrl.Builder url;

  VehicleRequest(String accessToken){
    this.accessToken = accessToken;
    this.url = HttpUrl.parse(
      "https://api.smartcar.com/v1.0/vehicles"
    ).newBuilder();
  }

  VehicleRequest setUrl(String url){
    this.url = HttpUrl.parse(url).newBuilder();
    return this;
  }

  private Request.Builder request(){
    return new Request.Builder()
      .url(this.url.build())
      .header("Authorization", "Bearer " + this.accessToken);
  }

  VehicleRequest vehicleId(String vehicleId){
    this.url.addPathSegment(vehicleId);
    return this;
  }

  /**
   * GETs from /v1.0/vehicles/id/endpoint, and returns the response body. 
   * 
   * @param  endpoint     
   * 
   * @return The response body from the API, which is a JSON
   *         string containing fields relevant to the endpoint.
   *         
   *         Some examples:
   *         /collision_sensor/  { "isTriggered": false }
   *         /engine/coolant/    { 
   *                               "level": 0.78, 
   *                               "temperature": 94.4  
   *                             }
   *         The airbags, doors, safety_locks, lights/interior, mirrors, 
   *         odometer/trip, seats, tires, wheels, wheels/speed, and windows
   *         endpoints return nested arrays
   *         
   * @throws Exceptions.SmartcarException
   */
  String get(String endpoint) 
  throws Exceptions.SmartcarException {
    this.url.addPathSegments(endpoint);
    return Util.call(request().build());
  }

  /**
   * POSTs to /v1.0/vehicles/id/endpoint.
   * 
   * @param endpoint
   * 
   * @param body Request body that describes what action the API should 
   *             take on the car. It typically looks like this:
   *                {
   *                  "action": "..."
   *                }
   *             However, certain endpoints accept an additional parameter, 
   *             such as charge/limit, charge/schedule, climate, mirrors, 
   *             and windows. A request body for climate will look like this:
   *             {
   *               "action": "START",
   *               "temperature": 20.3
   *             }
   *                    
   * @throws Exceptions.SmartcarException
   */
  void action(String endpoint, String body)
  throws Exceptions.SmartcarException {
    this.url.addPathSegments(endpoint);
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody formBody = RequestBody.create(JSON, body);
    Request request = request().post(formBody).build();
    Util.call(request);
  }

  /**
   * GETs the list of permissions currently associated with your application
   * @return The 
   * @throws Exceptions.SmartcarException [description]
   */
  String permissions()
  throws Exceptions.SmartcarException {
    this.url.addPathSegment("permissions");
    return Util.call(request().build());
  }

  /**
   * See permissions() for behavior
   * 
   * @param limit   Set the limit on the number of permissions to return
   *                default is 25, maximum is 50
   *                
   * @param offset  Set the index to start permission list at
   *                default is 0
   */
  String permissions(int limit, int offset)
  throws Exceptions.SmartcarException {
    //this.url += String.format("?limit=%s&offset=%s", limit, offset);
    this.url
    .addQueryParameter("limit", "" + limit)
    .addQueryParameter("offset", "" + offset);

    return permissions();
  }

  void disconnect()
  throws Exceptions.SmartcarException {
    this.url.addPathSegment("application");
    Util.call(request().delete().build());
  }

  /**
   * GETs the list of vehicles from /vehicles/.
   * Used by Smartcar.getVehicles
   * 
   * @return The response body from the API, which is a JSON string 
   *         containing the vehicles array and paging object. 
   *         It should look like this:
   *           {
   *             "vehicles": [
   *               "36ab27d0-fd9d-4455-823a-ce30af709ffc",
   *               "3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1"
   *             ],
   *             "paging": {
   *               "count": 25,
   *               "offset": 0
   *             }
   *           }
   *            
   * @throws Exceptions.SmartcarException
   */
  String vehicles()
  throws Exceptions.SmartcarException {
    return Util.call(request().url(this.url.build()).build());
  }

  /**
   * See vehicles() for behavior
   * 
   * @param limit   Set the limit on the number of vehicles to return
   *                [default: 10, maximum: 50]
   *                
   * @param offset  Set the index to start permission list at [default: 0]
   */
  String vehicles(int limit, int offset)
  throws Exceptions.SmartcarException {
    //this.url += String.format("?limit=%s&offset=%s", limit, offset);
    this.url
      .addQueryParameter("limit", "" + limit)
      .addQueryParameter("offset", "" + offset)
      .build();
    return vehicles();
  }
}