package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public Date getDataAge() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return format.parse(this.dataAge);
    }

    public String getUnitSystem() { return this.unitSystem; }
}
