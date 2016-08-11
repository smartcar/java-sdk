package com.smartcar.sdk;

import okhttp3.HttpUrl;
import com.google.gson.Gson;
import java.util.ArrayList;

public final class Smartcar {

  private final String id, secret, redirectUri;
  private final String[] scope;
  private final Gson gson = new Gson();
  private final AccessRequest access;
  private String vehicleUrl;

  public Smartcar(String id, String secret,
                  String redirectUri, String[] scope) {
    this.id = id;
    this.secret = secret;
    this.redirectUri = redirectUri;
    this.scope = scope;
    this.access = new AccessRequest(id, secret);
  }

  void setAccessUrl(String url){
    this.access.setUrl(url);
  }

  void setVehicleUrl(String url){
    this.vehicleUrl = url;
  }

  private VehicleRequest getVehicleRequest(String token){
    VehicleRequest vehicleRequest = new VehicleRequest(token);
    vehicleRequest.setUrl(this.vehicleUrl);
    return vehicleRequest;
  }

  private ArrayList<Vehicle> getVehicleArray(String token, String json){
    ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    ArrayList<String> vehicleIds = Util.getArray(json, "vehicles");
    for (String vehicleId : vehicleIds){
      Vehicle vehicle = getVehicle(token, vehicleId);
      vehicles.add(vehicle);
    }
    return vehicles;
  }

  /**
   * TODO add oauth state, and approval_prompt=[force|auto], and
   * maybe change the parameter to just the OEM name
   * 
   * This link is what allows the user to login to their OEM account
   * and accept your permissions defined in scope. If they accept, a
   * GET request will be sent to your redirectUri with code and 
   * state parameters
   * 
   * @param  baseUri 
   * 
   * @return oem authentication url
   */
  public String getAuthUrl(String baseUri) {
    HttpUrl parsed = HttpUrl.parse(baseUri);
    if (parsed != null){
      return parsed.newBuilder()
        .addPathSegments("oauth/authorize")
        .addQueryParameter("response_type", "code")
        .addQueryParameter("client_id", this.id)
        .addQueryParameter("redirect_uri", this.redirectUri)
        .addQueryParameter("scope", Util.join(this.scope))
        .build()
        .toString();
    }
    return null;
  }

  /**
   * Exchange an authentication code for an Access object.
   * 
   * @param  code the retrieved authorization code
   * 
   * @return an <Access> containing an access_token and refresh_token
   * 
   * @throws <Exceptions.SmartcarException>
   */
  public Access exchangeCode(String code) 
  throws Exceptions.SmartcarException {
    String json = this.access.code(code, this.redirectUri);
    return gson.fromJson(json, Access.class);
  }

  /**
   * Exchange a refresh token for a new <Access> object
   * @param  refreshToken
   * @return an <Access> containing an access_token and refresh_token
   * @throws <Exceptions.SmartcarException>
   */
  public Access exchangeToken(String refreshToken) 
  throws Exceptions.SmartcarException {

    String json = this.access.token(refreshToken);
    return gson.fromJson(json, Access.class);
  }
  
  /**
   * Get a list of the user's vehicles
   * @param  token A valid Access Token
   * @return The user's vehicles
   * @throws Exceptions.SmartcarException
   */
  public ArrayList<Vehicle> getVehicles(String token)
  throws Exceptions.SmartcarException {

    String json = getVehicleRequest(token).vehicles();
    return getVehicleArray(token, json);
  }

  /**
   * Get a list of the user's vehicles
   * @param  token  A valid Access Token
   * @param  limit  Set the limit on the number of vehicles to return
   *                [default: 10, maximum: 50]
   * @param  offset Set the index to start the vehicle list at [default: 0]
   */
  public ArrayList<Vehicle> getVehicles(String token, int limit, int offset)
  throws Exceptions.SmartcarException {

    String json = getVehicleRequest(token).vehicles(limit, offset);
    return getVehicleArray(token, json);
  }

  /**
   * Get an user's vehicle
   * @param  token     A valid Access Token
   * @param  vehicleId A valid vehicle id
   * @return           A new Vehicle object representing the user's vehicle
   */
  public Vehicle getVehicle(String token, String vehicleId){
    Vehicle vehicle = new Vehicle(token, vehicleId);
    if (this.vehicleUrl != null) vehicle.setUrl(this.vehicleUrl);
    return vehicle;
  }
}