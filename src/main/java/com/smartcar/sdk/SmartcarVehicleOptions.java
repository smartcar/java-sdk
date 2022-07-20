package com.smartcar.sdk;

import java.util.ArrayList;
import java.util.List;

/** Class encompassing any optional arguments for constructing a new Vehicle instance  */
public final class SmartcarVehicleOptions {
    private final String version;
    private final Vehicle.UnitSystem unitSystem;
    private final String origin;
    private final String flags;

    public static class Builder {
        private String version;
        private Vehicle.UnitSystem unitSystem;
        private String origin;
        private final List<String> flags;

        public Builder() {
            this.version = "2.0";
            this.unitSystem = Vehicle.UnitSystem.METRIC;
            this.origin = Smartcar.getApiOrigin();
            this.flags = new ArrayList<>();
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder unitSystem(Vehicle.UnitSystem unitSystem) {
            this.unitSystem = unitSystem;
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

        public Builder origin(String origin) {
            this.origin = origin;
            return this;
        }

        public SmartcarVehicleOptions build() {
            return new SmartcarVehicleOptions(this);
        }
    }

    private SmartcarVehicleOptions(Builder builder) {
        this.version = builder.version;
        this.unitSystem = builder.unitSystem;
        this.origin = builder.origin;
        if (builder.flags.size() > 0) {
            String[] flagStrings = builder.flags.toArray(new String[0]);
            this.flags = Utils.join(flagStrings, " ");
        } else {
            this.flags = null;
        }    
    }

    public String getVersion() {
        return this.version;
    }

    public Vehicle.UnitSystem getUnitSystem() {
        return this.unitSystem;
    }

    public String getFlags() { 
        return this.flags; 
    }

    public String getOrigin() {
        return this.origin;
    }
}

