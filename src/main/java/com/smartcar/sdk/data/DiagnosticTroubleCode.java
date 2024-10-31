package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;

/** Data class for an individual diagnostic trouble code. */
public class DiagnosticTroubleCode {
    @SerializedName("code")
    private String code;

    @SerializedName("timestamp")
    private String timestamp;

    /** No-argument constructor for deserialization. */
    public DiagnosticTroubleCode() {
    }

    /** Gets the diagnostic trouble code. */
    public String getCode() {
        return code;
    }

    /** Sets the diagnostic trouble code. */
    public void setCode(String code) {
        this.code = code;
    }

    /** Gets the timestamp associated with the code. */
    public String getTimestamp() {
        return timestamp;
    }

    /** Sets the timestamp associated with the code. */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
