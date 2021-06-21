package com.smartcar.sdk;

public final class SmartcarVehicleOptions {
    private final String version;
    private final Vehicle.UnitSystem unitSystem;
    private final String origin;

    public static class Builder {
        private String version;
        private Vehicle.UnitSystem unitSystem;
        private String origin;

        public Builder() {
            this.version = "2.0";
            this.unitSystem = Vehicle.UnitSystem.METRIC;
            String apiOrigin = System.getenv("SMARTCAR_API_ORIGIN");
            if (apiOrigin == null) {
                apiOrigin = Smartcar.API_ORIGIN;
            }
            this.origin = apiOrigin;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder unitSystem(Vehicle.UnitSystem unitSystem) {
            this.unitSystem = unitSystem;
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
    }

    public String getVersion() {
        return this.version;
    }

    public Vehicle.UnitSystem getUnitSystem() {
        return this.unitSystem;
    }

    public String getOrigin() {
        return this.origin;
    }
}

