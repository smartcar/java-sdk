package com.smartcar.sdk.data;

public class WebhookSubscription extends ApiData {
    private String webhookId;
    private String vehicleId;

    public String getWebhookId() {
        return webhookId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    @Override
    public String toString() {
        return this.getClass().getName()
                + "{"
                + "webhookId="
                + webhookId
                + ", vehicleId="
                + vehicleId
                + "}";
    }
}
