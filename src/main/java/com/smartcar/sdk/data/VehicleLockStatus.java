package com.smartcar.sdk.data;

import java.util.Arrays;

public class VehicleLockStatus extends ApiData {
    private boolean isLocked;
    private VehicleDoor[] doors;
    private VehicleWindow[] windows;
    private VehicleStorage[] storage;
    private VehicleSunroof[] sunroof;
    private VehicleChargingPort[] chargingPort;

    /**
     * Check if the vehicle currently locked.
     *
     * @return {boolean} True if the object is locked, otherwise false.
     */
    public boolean isLocked() {
        return this.isLocked;
    }

    /**
     * Returns the vehicle doors
     *
     * @return vehicle doors
     */
    public VehicleDoor[] getDoors() {
        return this.doors;
    }

    /**
     * Returns the vehicle's windows
     *
     * @return vehicle windows
     */
    public VehicleWindow[] getWindows() {
        return this.windows;
    }

    /**
     * Returns the vehicle's storage
     *
     * @return vehicle storage
     */
    public VehicleStorage[] getStorage() {
        return this.storage;
    }

    /**
     * Returns the vehicle's sunroofs
     *
     * @return vehicle sunroof
     */
    public VehicleSunroof[] getSunroof() {
        return this.sunroof;
    }

    /**
     * Returns the vehicle's charging ports
     *
     * @return vehicle charging port
     */
    public VehicleChargingPort[] getChargingPort() {
        return this.chargingPort;
    }

    /**
     * Returns a stringified representation of the VehicleLockStatus object.
     *
     * @return A string describing the lock status and other vehicle attributes.
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "{" +
                "isLocked=" + this.isLocked +
                ", doors=" + Arrays.toString(this.doors) +
                ", windows=" + Arrays.toString(this.windows) +
                ", storage=" + Arrays.toString(this.storage) +
                ", sunroof=" + Arrays.toString(this.sunroof) +
                ", chargingPort=" + Arrays.toString(this.chargingPort) +
                '}';
    }
}
