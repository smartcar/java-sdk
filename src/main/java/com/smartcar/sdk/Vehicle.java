package com.smartcar.sdk;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import com.google.gson.Gson;

class Vehicle {
  
  private final String accessToken, vehicleId;
  private final Request baseRequest;
  private final Gson gson = new Gson();

  public Vehicle(String accessToken, String vehicleId) {
      this.accessToken = accessToken;
      this.vehicleId = vehicleId;
      this.baseRequest = new Request.Builder()
        .header("Authorization", "Bearer " + this.accessToken)
        .build();
  }

  private Request getRequest(String url){
    return this.baseRequest.newBuilder()
      .url(url)
      .build();
  }

  private Request postRequest(String url, String body){
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    return this.baseRequest.newBuilder()
      .url(url)
      .post(RequestBody.create(JSON, body))
      .build();
  }

  public String[] permissions(){

    String endpoint = "permissions";

    String url = Util.makeApiUrl(this.vehicleId, endpoint);

    Request request = getRequest(url);

    String json = Util.call(request);

    // get permissions out of json.permissions[]
    return null;
  }

  public Api.Info info() {

    String endpoint = "";

    String url = Util.makeApiUrl(this.vehicleId, endpoint);

    Request request = getRequest(url);

    String json = Util.call(request);

    return gson.fromJson(json, Api.Info.class);
  }

  public Api.Accelerometer accelerometer() {

    String endpoint = "accelerometer";

    String url = Util.makeApiUrl(this.vehicleId, endpoint);

    Request request = getRequest(url);

    String json = Util.call(request);

    return gson.fromJson(json, Api.Accelerometer.class);
  }

  public Api.Airbag[] airbags() {

    String endpoint = "airbags";

    String url = Util.makeApiUrl(this.vehicleId, endpoint);

    Request request = getRequest(url);

    String json = Util.call(request);

    return gson.fromJson(json, Api.Airbag[].class);
  }

  /*
  ...
   */

  public void adjustMirrors(Api.Mirror[] mirrors){

    String endpoint = "mirrors";

    String url = Util.makeApiUrl(this.vehicleId, endpoint);

    String body = "{\"action\": \"TILT\", \"mirrors\": [...]}";

    Request request = postRequest(url, body);

    Util.call(request);
  }

  public void startPanic(){

    String endpoint = "panic";

    String url = Util.makeApiUrl(this.vehicleId, endpoint);

    String body = "{\"action\": \"START\"}";

    Request request = postRequest(url, body);

    Util.call(request);
  }

  public void stopPanic(){

    String endpoint = "panic";

    String url = Util.makeApiUrl(this.vehicleId, endpoint);

    String body = "{\"action\": \"STOP\"}";

    Request request = postRequest(url, body);

    Util.call(request);
  }

}
