package com.smartcar.sdk;

import com.google.gson.JsonObject;

import java.util.List;


public final class SmartcarAuthOptions {
    private String flags = null;

    public static class Builder {
        private final List<String> flags;

        public Builder() {
            this.flags = null;
        }

        public Builder addFlag(String key, String value) {
            this.flags.add(key + ":" + value);
            return this;
        }

        public Builder addFlag(String key, boolean value) {
            this.flags.add(key + ":" + value);
            return this;
        }

        public SmartcarAuthOptions build() {

            return new SmartcarAuthOptions(this);
        }
    }

    private SmartcarAuthOptions(Builder builder) {
        if (builder.flags != null) {
            String[] flagStrings = builder.flags.toArray(new String[0]);
            this.flags = Utils.join(flagStrings, " ");
        }
    }

    public String getFlags() {
        return this.flags;
    }
}
