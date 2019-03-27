package com.smartcar.sdk;

import okhttp3.HttpUrl;
import okhttp3.Request;
import org.mockito.ArgumentMatcher;

public class CompatibilityRequest extends ArgumentMatcher<Request> {
    private HttpUrl getHttpUrl() {
      String baseurl = "https://api.smartcar.com/v1.0";
      HttpUrl url = HttpUrl.parse(baseurl).newBuilder()
        .addPathSegment("compatibility")
        .addQueryParameter("vin", "vin")
        .addQueryParameter("scope", "read_location read_odometer")
        .build();
      return url; 
    } 
    
    @Override
    public boolean matches(Object arg) {
        HttpUrl url = getHttpUrl();
        Request request = (Request)arg;
        return url.equals(request.url());
    }
}
