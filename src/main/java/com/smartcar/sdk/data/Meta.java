package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.Date;

public class Meta {
    @SerializedName("sc-request-id")
    private String requestId;
    @SerializedName("sc-data-age")
    private String dataAge;
    @SerializedName("sc-unit-system")
    private String unitSystem;

    public String getRequestId() { return this.requestId; }

    public Date getDataAge() {
        Instant date = Instant.parse(this.dataAge);
        return Date.from(date);
    }

    public String getUnitSystem() { return this.unitSystem; }
}
