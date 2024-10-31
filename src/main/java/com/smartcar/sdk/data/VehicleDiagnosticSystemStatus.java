package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Data class for the diagnostic system status of a vehicle. */
public class VehicleDiagnosticSystemStatus extends ApiData {
    @SerializedName("systems")
    private List<DiagnosticSystem> systems;

    /** No-argument constructor for deserialization. */
    public VehicleDiagnosticSystemStatus() {
    }

    /** Returns the list of diagnostic systems. */
    public List<DiagnosticSystem> getSystems() {
        return systems;
    }

    /** Sets the list of diagnostic systems. */
    public void setSystems(List<DiagnosticSystem> systems) {
        this.systems = systems;
    }
}
