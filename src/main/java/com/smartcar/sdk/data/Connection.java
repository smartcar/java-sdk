package com.smartcar.sdk.data;

public class Connection {
    private String vehicleId;
    private String userId;
    private String connectedAt;

    public String getVehicleId() {
        return vehicleId;
    }

    public String getUserId() {
        return userId;
    }

    public String getConnectedAt() {
        return connectedAt;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "vehicleId='" + vehicleId + '\'' +
                ", userId='" + userId + '\'' +
                ", createdAt='" + connectedAt + '\'' +
                '}';
    }
}
