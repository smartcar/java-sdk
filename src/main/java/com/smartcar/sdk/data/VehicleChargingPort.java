package com.smartcar.sdk.data;

public class VehicleChargingPort extends ApiData {
    private String type;
    private String status;

    /**
     * Returns the charging port type
     *
     * @return charging port type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the charging port status
     *
     * @return charging port status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @return a stringified representation of VehicleChargingPort
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "type=" + type + ", status=" + status + '}';
    }
}
