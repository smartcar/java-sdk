package com.smartcar.sdk;

import java.util.ArrayList;
import java.util.List;


public final class SmartcarAuthOptions {
    private String flags;

    public static class Builder {
        private final List<String> flags;

        public Builder() {
            this.flags = new ArrayList<>();
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
        if (builder.flags.size() > 0) {
            String[] flagStrings = builder.flags.toArray(new String[0]);
            this.flags = Utils.join(flagStrings, " ");
        } else {
            this.flags = null;
        }
    }

    public String getFlags() {
        return this.flags;
    }
}
