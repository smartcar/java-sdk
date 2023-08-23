package com.smartcar.sdk.data;

public class VehicleStorage extends ApiData {
    private String type;
    private String status;

    /**
     * Returns the storage type
     *
     * @return storage type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the storage status
     *
     * @return storage status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @return a stringified representation of VehicleStorage
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "type=" + type + ", status=" + status + '}';
    }
}
