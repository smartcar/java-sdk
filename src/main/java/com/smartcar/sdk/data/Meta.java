package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;
import com.smartcar.sdk.SmartcarException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

public class Meta {
    @SerializedName("sc-request-id")
    private String requestId;
    
    // Timestamp of when the data was originally created/reported by the vehicle
    @SerializedName("sc-data-age")
    private String dataAge = null;
    
    @SerializedName("sc-unit-system")
    private String unitSystem;
    
    // Timestamp of when Smartcar's system last processed/fetched the data
    @SerializedName("sc-fetched-at")
    private String fetchedAt = null;

    public String getRequestId() { return this.requestId; }

    public Date getDataAge() throws SmartcarException {
        if (this.dataAge == null) {
            return null;
        }

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return format.parse(this.dataAge);
        } catch (ParseException ex) {
            throw new SmartcarException.Builder().type("SDK_ERROR").description(ex.getMessage()).build();
        }
    }

    public String getUnitSystem() { return this.unitSystem; }
    
    public Date getFetchedAt() throws SmartcarException {
        if (this.fetchedAt == null) {
            return null;
        }

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return format.parse(this.fetchedAt);
        } catch (ParseException ex) {
            throw new SmartcarException.Builder().type("SDK_ERROR").description(ex.getMessage()).build();
        }
    }
}
