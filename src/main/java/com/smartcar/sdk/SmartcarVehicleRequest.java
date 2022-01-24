package com.smartcar.sdk;

import java.util.ArrayList;
import java.util.List;

public final class SmartcarVehicleRequest {
    private final String method;
    private final String path;
    private final String body;
    private final String headers;
    private final String version;
    // TODO: headers

    public static class Builder {
        private String method;
        private String path;
        private String body;
        private final List<String> headers;

        public Builder() {
            this.method = "";
            this.path = "/";
            this.body = "";
            this.headers = new ArrayList<>();
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder addHeader(String key, String value){
            this.headers.add(key + ":" + value);
            return this;
        }

        public Builder addHeader(String key, Boolean value){
            this.headers.add(key + ":" + value);
            return this;
        }

        public SmartcarVehicleRequest build() throws Exception {
            if (this.clientId == null) {
                throw new Exception("clientId must be defined");
            }
            if (this.clientSecret == null) {
                throw new Exception("clientSecret must be defined");
            }
            return new SmartcarVehicleRequest(this);
        }
    }
}
