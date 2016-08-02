package com.smartcar.sdk;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.FormBody;
import com.google.gson.Gson;
import java.util.ArrayList;

final public class Smartcar {

  final static String AUTH = "https://auth.smartcar.com/oauth/token";

  private final String clientId, clientSecret, redirectUri;
  private final String[] scope;
  private final Gson gson = new Gson();

  public Smartcar(String clientId, String clientSecret,
                  String redirectUri, String[] scope) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
    this.scope = scope;
  }

  private Request makeAuthRequest(FormBody formBody){
    return new Request.Builder()
      .url(AUTH)
      .header("Authorization", 
              Credentials.basic(this.clientId, this.clientSecret))
      .post(formBody)
      .build();
  }

  public String getAuthUrl(String baseUri) {
    HttpUrl parsed = HttpUrl.parse(baseUri);
    if (parsed != null){
      return parsed.newBuilder()
        .addPathSegments("oauth/authorize")
        .addQueryParameter("response_type", "code")
        .addQueryParameter("client_id", this.clientId)
        .addQueryParameter("redirect_uri", this.redirectUri)
        .addQueryParameter("scope", Util.join(this.scope))
        .build()
        .toString();
    }
    return null;
  }

  public Access exchangeCode(String code) 
    throws IOException {

    FormBody formBody = new FormBody.Builder()
      .add("grant_type", "authorization_code")
      .add("code", code)
      .add("redirect_uri", this.redirectUri)
      .build();

    String response = Util.call(makeAuthRequest(formBody));
    return gson.fromJson(response, Access.class);
  }

  public Access exchangeToken(String refreshToken) 
    throws IOException {

    FormBody formBody = new FormBody.Builder()
      .add("grant_type", "refresh_token")
      .add("refresh_token", refreshToken)
      .build();

    String response = Util.call(makeAuthRequest(formBody));
    return gson.fromJson(response, Access.class);
  }
  
  public ArrayList<Vehicle> getVehicles(String accessToken){
    Request request;
    String response;
    ArrayList<Vehicle> vehicles;
    ArrayList<String> ids;

    /* https://api.smartcar.com/v1.0/vehicles */
    request = new Request.Builder()
      .url(Util.makeApiUrl())
      .build();

    /* 
    {
      "vehicles": [
        "36ab27d0-fd9d-4455-823a-ce30af709ffc",
        "3cd4ffb8-b0f6-45cf-9a4c-47d2102bb6b1"
      ],
      "paging": {
        "count": 10,
        "offset": 0
      }
    }
    */
    
    // vehicleIds = ...
    /*for (String vehicleId : vehicleIds ){

    }*/
    return null;
  }

  public Vehicle getVehicle(String accessToken, String vehicleId){
    return new Vehicle(accessToken, vehicleId);
  }
}