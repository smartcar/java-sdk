package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("sc-request-id")
    private String requestId;
    @SerializedName("sc-data-age")
    private String dataAge;
    @SerializedName("sc-unit-system")
    private String unitSystem;

    public String getRequestId() { return this.requestId; }

    public String getDataAge() { return this.dataAge; }

    public String getUnitSystem() { return this.unitSystem; }
}
