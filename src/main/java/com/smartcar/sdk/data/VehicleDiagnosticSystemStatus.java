package com.smartcar.sdk.data;

import java.util.List;

/** Data class for the diagnostic system status of a vehicle. */
public class VehicleDiagnosticSystemStatus extends ApiData {
    private List<DiagnosticSystem> systems;

    /** No-argument constructor for deserialization. */
    public VehicleDiagnosticSystemStatus() {
    }

    /** Returns the list of diagnostic systems. */
    public List<DiagnosticSystem> getSystems() {
        return this.systems;
    }
}
