package com.smartcar.sdk.data;

public class ActionResponse extends ApiData {
    private String status;

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.getClass().getName()
                + "{"
                + "status="
                + status
                + "}";
    }
}
