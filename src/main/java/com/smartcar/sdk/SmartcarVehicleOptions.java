package com.smartcar.sdk;

import java.util.ArrayList;
import java.util.List;

/** Class encompassing any optional arguments for constructing a new Vehicle instance  */
public final class SmartcarVehicleOptions {
    private final String version;
    private final Vehicle.UnitSystem unitSystem;
    private final String origin;
    private final String v3Origin;
    private final String flags;

    public static class Builder {
        private String version;
        private Vehicle.UnitSystem unitSystem;
        private String origin;
        private String v3Origin;
        private final List<String> flags;

        public Builder() {
            this.version = "2.0";
            this.unitSystem = Vehicle.UnitSystem.METRIC;
            this.origin = Smartcar.getApiOrigin();
            this.v3Origin = Smartcar.getApiOrigin("3");
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

        public Builder v3Origin(String v3Origin) {
            this.v3Origin = v3Origin;
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
        this.v3Origin = builder.v3Origin;
        if (!builder.flags.isEmpty()) {
            this.flags = String.join(" ", builder.flags);
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

    public String getV3Origin() {
        return this.v3Origin;
    }
}
