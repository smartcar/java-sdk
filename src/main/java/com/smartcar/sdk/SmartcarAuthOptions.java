package com.smartcar.sdk;

import com.google.gson.JsonObject;


public final class SmartcarAuthOptions {
    private final boolean forcePrompt;
    private final boolean singleSelectEnabled;
    private final String singleSelectVin;
    private final String state;
    private final String makeByPass;
    private final JsonObject flags;
    private final String version;
    private final String origin;

    public static class Builder {
        private boolean forcePrompt;
        private boolean singleSelectEnabled;
        private String singleSelectVin;
        private String state;
        private String makeByPass;
        private JsonObject flags;
        private String version;
        private String origin;

        public Builder() {
            this.forcePrompt = false;
            this.singleSelectEnabled = false;
            this.singleSelectVin = "";
            this.state = "";
            this.makeByPass = "";
            this.version = "1.0";
            this.origin = AuthClient.urlAccessToken;
        }

        public Builder forcePrompt(boolean forcePrompt) {
            this.forcePrompt = forcePrompt;
            return this;
        }

        public Builder singleSelect(boolean enabled) {
            this.singleSelectEnabled = enabled;
            return this;
        }

        public Builder singleSelect(String vin) {
            this.singleSelectEnabled = true;
            this.singleSelectVin = vin;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder makeBypass(String make) {
            this.makeByPass = make;
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

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Set Smartcar Authenticate base URL for debugging purposes
         * @param origin String
         * @return SmartcarAuthOptions
         */
        public Builder origin(String origin) {
            this.origin = origin;
            return this;
        }

        public SmartcarAuthOptions build() {
            return new SmartcarAuthOptions(this);
        }
    }

    private SmartcarAuthOptions(Builder builder) {
        this.forcePrompt = builder.forcePrompt;
        this.singleSelectEnabled = builder.singleSelectEnabled;
        this.singleSelectVin = builder.singleSelectVin;
        this.state = builder.state;
        this.makeByPass = builder.makeByPass;
        this.flags = builder.flags;
        this.version = builder.version;
        this.origin = builder.origin;
    }

    public boolean getForcePrompt() {
        return this.forcePrompt;
    }

    public boolean getSingleSelectEnabled() {
        return this.singleSelectEnabled;
    }

    public String getSingleSelectVin() {
        return this.singleSelectVin;
    }

    public String getState() {
        return this.state;
    }

    public String getMakeByPass() {
        return this.makeByPass;
    }

    public JsonObject getFlags() {
        return this.flags;
    }

    public String getVersion() {
        return this.version;
    }

    public String getOrigin() {
        return this.origin;
    }
}
