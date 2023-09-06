package com.smartcar.sdk.data;

public class VehicleDoor extends ApiData {
    private String type;
    private String status;

    /**
     * Returns the door type
     *
     * @return door type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the door status
     *
     * @return door status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @return a stringified representation of VehicleDoor
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "type=" + type + ", status=" + status + '}';
    }
}
