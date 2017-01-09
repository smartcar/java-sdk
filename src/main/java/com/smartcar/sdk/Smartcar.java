package com.smartcar.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Smartcar {

  private final String clientId, clientSecret, redirectUri;
  private final String[] scope;
  private final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  private final AccessRequest access;
  private String vehicleUrl;

  /**
   * Default constructor.
   *
   * @param clientId Application's client ID, provided by Smartcar
   * @param clientSecret Application's client secret, provided by Smartcar
   * @param redirectUri Application's redirect URI
   * @param scope List of scopes. See Smartcar's list of permissions
   */
  public Smartcar(String clientId, String clientSecret,
                  String redirectUri, String[] scope) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
    this.scope = scope;
    this.access = new AccessRequest(clientId, clientSecret);
  }

  void setBaseAccessUrl(String url) {
    this.access.setBaseUrl(url);
  }

  void setBaseVehicleUrl(String url) {
    this.vehicleUrl = url;
  }

  /**
   * Create a VehicleRequest object.
   *
   * @param token The access token
   */
  private VehicleRequest getVehicleRequest(String token) {
    VehicleRequest vehicleRequest = new VehicleRequest(token);
    if (this.vehicleUrl != null)
      vehicleRequest.setBaseUrl(this.vehicleUrl);
    return vehicleRequest;
  }

  /**
   * Generate the OAuth authentication URL for the specified OEM.
   *
   * @param  oem name of an OEM
   */
  public AuthUrl getAuthUrl(String oem) {
    return new AuthUrl(oem, this.clientId, this.redirectUri, Util.join(this.scope));
  }

  /**
   * Exchange an authentication code for an Access object.
   *
   * @param  code the retrieved authorization code
   * @throws <Exceptions.SmartcarException>
   */
  public Access exchangeCode(String code)
  throws Exceptions.SmartcarException {
    String json = this.access.code(code, this.redirectUri);
    System.out.println(json);
    Access accessObj = gson.fromJson(json, Access.class);
    System.out.println(accessObj.getAccessToken());
    return gson.fromJson(json, Access.class);
  }

  /**
   * Exchange a refresh token for a new Access object.
   *
   * @param  refreshToken
   * @throws <Exceptions.SmartcarException>
   */
  public Access exchangeToken(String refreshToken)
  throws Exceptions.SmartcarException {
    String json = this.access.token(refreshToken);
    return gson.fromJson(json, Access.class);
  }

  /**
   * Get a list of the user's vehicles.
   *
   * @param  token A valid Access Token
   * @throws <Exceptions.SmartcarException>
   */
  public Api.Vehicles getVehicles(String token)
  throws Exceptions.SmartcarException {
    String json = getVehicleRequest(token).vehicles();
    return gson.fromJson(json, Api.Vehicles.class);
  }

  /**
   * Get a list of the user's vehicles.
   *
   * @param  token  A valid Access Token
   * @param  limit  Set the limit on the number of vehicles to return
   *                [default: 10, maximum: 50]
   * @param  offset Set the index to start the vehicle list at [default: 0]
   * @throws <Exceptions.SmartcarException>
   */
  public Api.Vehicles getVehicles(String token, int limit, int offset)
  throws Exceptions.SmartcarException {

    String json = getVehicleRequest(token).vehicles(limit, offset);
    return gson.fromJson(json, Api.Vehicles.class);
  }
}