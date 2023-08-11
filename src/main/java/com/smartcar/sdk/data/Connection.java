package com.smartcar.sdk.data;

public class Connection {
    private String vehicleId;
    private String userId;
    private String createdAt;

    public String getVehicleId() {
        return vehicleId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "vehicleId='" + vehicleId + '\'' +
                ", userId='" + userId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
