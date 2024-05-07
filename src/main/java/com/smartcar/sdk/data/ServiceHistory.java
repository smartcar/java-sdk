package com.smartcar.sdk.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/** POJO for Service History of a Vehicle */
public class ServiceHistory extends ApiData {
    @SerializedName("items")
    private List<ServiceRecord> items;

    public ServiceHistory() {
        // no-arg constructor for deserialization
    }

    public ServiceHistory(List<ServiceRecord> items) {
        this.items = items;
    }

    /**
     * Returns the list of service records.
     *
     * @return List of service records
     */
    public List<ServiceRecord> getItems() {
        return this.items;
    }
}
