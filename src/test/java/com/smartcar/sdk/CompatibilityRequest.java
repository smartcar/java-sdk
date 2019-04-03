package com.smartcar.sdk;

import okhttp3.HttpUrl;
import okhttp3.Request;
import org.mockito.ArgumentMatcher;

public class CompatibilityRequest extends ArgumentMatcher<Request> {
    @Override
    public boolean matches(Object arg) {
        String url = "https://api.smartcar.com/v1.0/compatibility?vin=vin&scope=read_location%20read_odometer";
        Request request = (Request)arg;
        return url.equals(request.url().toString());
    }
}
