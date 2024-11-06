package com.smartcar.sdk.data;


/** Data class for an individual diagnostic trouble code. */
public class DiagnosticTroubleCode {
    private String code;
    private String timestamp;

    /** No-argument constructor for deserialization. */
    public DiagnosticTroubleCode() {
    }

    /** Gets the diagnostic trouble code. */
    public String getCode() {
        return this.code;
    }

    /** Gets the timestamp associated with the code. */
    public String getTimestamp() {
        return this.timestamp;
    }
}
