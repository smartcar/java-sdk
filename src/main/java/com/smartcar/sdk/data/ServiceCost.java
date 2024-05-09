package com.smartcar.sdk.data;

public class ServiceCost {
    private double totalCost;
    private String currency;

    public ServiceCost(double totalCost, String currency) {
        this.totalCost = totalCost;
        this.currency = currency;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
