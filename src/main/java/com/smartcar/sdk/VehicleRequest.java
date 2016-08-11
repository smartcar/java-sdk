package com.smartcar.sdk;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.RequestBody;

class VehicleRequest {

  private String token, url, vehicleId;

  VehicleRequest(String token){
    this.token = token;
    this.url = "https://api.smartcar.com/v1.0/vehicles";
  }

  VehicleRequest(String token, String vehicleId){
    this(token);
    this.vehicleId = vehicleId;
  }

  Request.Builder request(String url){
    return new Request.Builder()
      .header("Authorization", "Bearer " + token)
      .url(url); 
  }

  void setBaseUrl(String url){
    this.url = url;
  }

  private String formatUrl(String endpoint){
    return this.url + '/' + this.vehicleId + '/' + endpoint;
  }

  private String paging(int limit, int offset){
    return "?limit=" + limit + "&offset=" + offset;
  }

  String get(String endpoint) 
  throws Exceptions.SmartcarException {
    String url = formatUrl(endpoint);
    return Util.call(request(url).build());
  }

  void action(String endpoint, String body) 
  throws Exceptions.SmartcarException {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody formBody = RequestBody.create(JSON, body);
    String url = formatUrl(endpoint);
    Util.call(request(url).post(formBody).build());
  }

  String permissions()
  throws Exceptions.SmartcarException {
    String url = formatUrl("permissions");
    return Util.call(request(url).build());
  }

  String permissions(int limit, int offset)
  throws Exceptions.SmartcarException {
    String url = formatUrl("permissions") + paging(limit, offset);
    return Util.call(request(url).build());
  }

  void disconnect()
  throws Exceptions.SmartcarException {
    String url = formatUrl("application");
    Util.call(request(url).delete().build());
  }

  String vehicles()
  throws Exceptions.SmartcarException {
    return Util.call(request(this.url).build());
  }

  String vehicles(int limit, int offset)
  throws Exceptions.SmartcarException {
    String url = this.url + paging(limit, offset);
    return Util.call(request(url).build());
  }
}