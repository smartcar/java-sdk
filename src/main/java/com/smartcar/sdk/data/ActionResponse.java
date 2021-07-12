package com.smartcar.sdk.data;

public class ActionResponse extends ApiData {
    private String status;
    private String message;

    public String getStatus() {
        return this.status;
    }

    public String getMessage() { return this.message; }

    @Override
    public String toString() {
        return this.getClass().getName()
                + "{"
                + "status="
                + status
                + ", message="
                + message
                + "}";
    }
}
