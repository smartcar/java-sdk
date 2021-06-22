package com.smartcar.sdk;

import com.google.gson.JsonObject;

public final class SmartcarCompatibilityRequest {
    private final String vin;
    private final String[] scope;
    private final String country;
    private final String version;
    private final JsonObject flags;
    private final String clientId;
    private final String clientSecret;

    public static class Builder {
        private String vin;
        private String[] scope;
        private String country;
        private String version;
        private final JsonObject flags;
        private String clientId;
        private String clientSecret;

        public Builder() {
            this.vin = "";
            this.scope = null;
            this.country = "US";
            this.version = "1.0";
            this.flags = new JsonObject();
            this.clientId = System.getenv("SMARTCAR_CLIENT_ID");
            this.clientSecret = System.getenv("SMARTCAR_CLIENT_SECRET");
        }

        public Builder vin(String vin) {
            this.vin = vin;
            return this;
        }

        public Builder scope(String[] scope) {
            this.scope = scope;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder version(String country) {
            this.version = version;
            return this;
        }

        public Builder addFlag(String key, String value) {
            this.flags.addProperty(key, value);
            return this;
        }

        public Builder addFlag(String key, boolean value) {
            this.flags.addProperty(key, value);
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public SmartcarCompatibilityRequest build() throws SmartcarException {
            if (this.clientId == null) {
                throw new SmartcarException.Builder()
                        .type("INVALID_REQUEST")
                        .description("clientId must be defined").build();
            }
            if (this.clientSecret == null) {
                throw new SmartcarException.Builder()
                        .type("INVALID_REQUEST")
                        .description("clientSecret must be defined").build();
            }
            return new SmartcarCompatibilityRequest(this);
        }
    }

    private SmartcarCompatibilityRequest(Builder builder) {
        this.vin = builder.vin;
        this.scope = builder.scope;
        this.country = builder.country;
        this.version = builder.version;
        this.flags = builder.flags;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
    }

    public String getVin() {
        return this.vin;
    }

    public String[] getScope() { return this.scope; }

    public String getCountry() { return this.country; }

    public String getVersion() { return this.version; }

    public JsonObject getFlags() { return this.flags; }

    public String getClientId() { return this.clientId; }

    public String getClientSecret() { return this.clientSecret; }
}