package com.smartcar.sdk.data;

public class VehicleWindow extends ApiData {
    private String type;
    private String status;

    /**
     * Returns the window type
     *
     * @return window type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the window status
     *
     * @return window status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @return a stringified representation of VehicleWindow
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "type=" + type + ", status=" + status + '}';
    }
}
