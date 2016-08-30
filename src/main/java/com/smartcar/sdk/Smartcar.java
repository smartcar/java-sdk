package com.smartcar.sdk;

import com.google.gson.Gson;

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

  void setBaseAccessUrl(String url){
    this.access.setBaseUrl(url);
  }

  void setBaseVehicleUrl(String url){
    this.vehicleUrl = url;
  }

  private VehicleRequest getVehicleRequest(String token){
    VehicleRequest vehicleRequest = new VehicleRequest(token);
    if (this.vehicleUrl != null)
      vehicleRequest.setBaseUrl(this.vehicleUrl);
    return vehicleRequest;
  }

  private Vehicle[] getVehicleArray(String token, String json){
    String[] vehicleIds = gson.fromJson(json, Api.Vehicles.class).vehicles;
    Vehicle[] vehicles = new Vehicle[vehicleIds.length];
    for (int i=0; i<vehicleIds.length; i++){
      vehicles[i] = getVehicle(token, vehicleIds[i]);
    }
    return vehicles;
  }

  /**
   * Generate a OAuth authentication URL for the specified OEM
   *
   * @param  oem name of an OEM
   *
   * @return oem authentication url
   */
  public AuthUrl getAuthUrl(String oem) {
    return new AuthUrl(oem, this.id, this.redirectUri, Util.join(this.scope));
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
  public Vehicle[] getVehicles(String token)
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
  public Vehicle[] getVehicles(String token, int limit, int offset)
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
    if (this.vehicleUrl != null)
      vehicle.setBaseUrl(this.vehicleUrl);
    return vehicle;
  }
}