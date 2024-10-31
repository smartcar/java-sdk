package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;

/** Data class for an individual diagnostic system. */
public class DiagnosticSystem {
    @SerializedName("systemId")
    private String systemId;

    @SerializedName("status")
    private String status;

    @SerializedName("description")
    private String description;

    /** No-argument constructor for deserialization. */
    public DiagnosticSystem() {
    }

    /** Gets the system ID. */
    public String getSystemId() {
        return systemId;
    }

    /** Sets the system ID. */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /** Gets the status of the system. */
    public String getStatus() {
        return status;
    }

    /** Sets the status of the system. */
    public void setStatus(String status) {
        this.status = status;
    }

    /** Gets the description of the system. */
    public String getDescription() {
        return description;
    }

    /** Sets the description of the system. */
    public void setDescription(String description) {
        this.description = description;
    }
}
