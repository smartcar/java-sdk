package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;
import com.smartcar.sdk.SmartcarException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

/**
 * A container for the metadata in the response from a Smartcar API request.
 */
public class Meta {
    /** Unique identifier for the API request that can be used for debugging */
    @SerializedName("sc-request-id")
    private String requestId;
    
    /** ISO 8601 timestamp of when the data was retrieved from the vehicle */
    @SerializedName("sc-data-age")
    private String dataAge = null;
    
    /** Unit system used in vehicle data (imperial or metric) */
    @SerializedName("sc-unit-system")
    private String unitSystem;
    
    /** ISO 8601 timestamp of when the data was fetched by Smartcar */
    @SerializedName("sc-fetched-at")
    private String fetchedAt = null;

    /**
     * Returns the request ID.
     *
     * @return the request ID
     */
    public String getRequestId() { return this.requestId; }

    /**
     * Returns the data age as a Date.
     *
     * @return the data age
     * @throws SmartcarException if there is a parsing error
     */
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

    /**
     * Returns the unit system.
     *
     * @return the unit system
     */
    public String getUnitSystem() { return this.unitSystem; }
    
    /**
     * Returns the fetched at timestamp as a Date.
     *
     * @return the fetched at timestamp
     * @throws SmartcarException if there is a parsing error
     */
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
