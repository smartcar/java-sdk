package com.smartcar.sdk.data;

/** Data class for an individual diagnostic system. */
public class DiagnosticSystem {
    private String systemId;
    private String status;
    private String description;

    /** No-argument constructor for deserialization. */
    public DiagnosticSystem() {
    }

    /** Gets the system ID. */
    public String getSystemId() {
        return this.systemId;
    }

    /** Gets the status of the system. */
    public String getStatus() {
        return this.status;
    }

    /** Gets the description of the system. */
    public String getDescription() {
        return this.description;
    }
}
