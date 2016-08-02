package com.smartcar.sdk;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.OkHttpClient;

final public class Util {

  private final static String API = "https://api.smartcar.com";
  private final static String VERSION = "1.0";
  private final static OkHttpClient client = new OkHttpClient();


  /**
   * Returns a URL that can be used in requests to the Smartcar API
   * @return A Smartcar API URL
   */
  static String makeApiUrl() {
    return String.format("%s/v%s/vehicles", API, VERSION);
  }

  /**
   * Returns a URL that can be used in requests to the Smartcar API
   * @param  vehicleId Id of the vehicle to append to the URL
   * @param  endpoint  Endpoint to append to the url
   * @return           A Smartcar API URL
   */
  static String makeApiUrl(String vehicleId, String endpoint) {
    return makeApiUrl() + '/' + vehicleId + '/' + endpoint;
  }

  /**
   * calls the api, and returns the response on success, or 
   * throws an appropriate error
   * @param  request Request to send
   * @return the response body
   * @throws SmartcarError
   */
  static String call(Request request){
    Response response = client.newCall(request).execute();
    if (!response.isSuccessful()){
      // throw appropriate error based on response.code()
      switch(response.code()) {
        case 400: throw new ValidationException();
        case 401: throw new AuthenticationException();
        case 403: throw new PermissionException();
        case 404: throw new ResourceNotFoundException();
        case 409: throw new StateException();
        case 429: throw new RateLimitingException();
        case 430: throw new MonthlyLimitExceeded();
        case 500: throw new ServerException();
        case 501: throw new NotCapableException();
        case 504: throw new GatewayTimeoutException();
      }
    } else {
      ResponseBody body = response.body();
      response.close();
      return body.string();
    }
  }

  /**
   * join an array of strings with spaces
   * @param  strings Array of strings to join
   * @return         Joined array
   */
  static String join(String[] strings) {
    if (strings.length == 0) 
      return "";
    if (strings.length == 1)
      return strings[0];

    String joinedString = "";
    for (int i=0; i<strings.length - 1; i++){
       joinedString += strings[i] + " ";
    }
    joinedString += strings[strings.length - 1];
    return joinedString;
  }

}