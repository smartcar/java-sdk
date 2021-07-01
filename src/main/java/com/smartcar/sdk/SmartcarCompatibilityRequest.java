package com.smartcar.sdk;

import java.util.ArrayList;
import java.util.List;

public final class SmartcarCompatibilityRequest {
    private final String vin;
    private final String[] scope;
    private final String country;
    private final String version;
    private final String flags;
    private final String clientId;
    private final String clientSecret;

    public static class Builder {
        private String vin;
        private String[] scope;
        private String country;
        private String version;
        private final List<String> flags;
        private String clientId;
        private String clientSecret;

        public Builder() {
            this.vin = "";
            this.scope = null;
            this.country = "US";
            this.version = "1.0";
            this.flags = new ArrayList<>();
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

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder addFlag(String key, String value) {
            this.flags.add(key + ":" + value);
            return this;
        }

        public Builder addFlag(String key, boolean value) {
            this.flags.add(key + ":" + value);
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
                        .type("INVALID_COMPATIBILITY_REQUEST")
                        .description("clientId must be defined").build();
            }
            if (this.clientSecret == null) {
                throw new SmartcarException.Builder()
                        .type("INVALID_COMPATIBILITY_REQUEST")
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
        if (builder.flags.size() > 0) {
            String[] flagStrings = builder.flags.toArray(new String[0]);
            this.flags = Utils.join(flagStrings, " ");
        } else {
            this.flags = null;
        }
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
    }

    public String getVin() {
        return this.vin;
    }

    public String[] getScope() { return this.scope; }

    public String getCountry() { return this.country; }

    public String getVersion() { return this.version; }

    public String getFlags() { return this.flags; }

    public String getClientId() { return this.clientId; }

    public String getClientSecret() { return this.clientSecret; }
}