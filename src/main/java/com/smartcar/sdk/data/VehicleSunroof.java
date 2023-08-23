package com.smartcar.sdk.data;

public class VehicleSunroof extends ApiData {
    private String type;
    private String status;

    /**
     * Returns the sunroof type
     *
     * @return sunroof type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the sunroof status
     *
     * @return sunroof status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @return a stringified representation of VehicleSunroof
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "type=" + type + ", status=" + status + '}';
    }
}
