package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Meta {
    @SerializedName("SC-Request-Id")
    private String requestId;
    @SerializedName("SC-Data-Age")
    private Date dataAge;
    @SerializedName("SC-Unit-System")
    private String unitSystem;

    public String getRequestId() { return this.requestId; }

    public Date getDataAge() { return this.dataAge; }

    public String getUnitSystem() { return this.unitSystem; }
}
