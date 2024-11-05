package com.smartcar.sdk.data;

import java.util.List;

/** Data class for the diagnostic trouble codes of a vehicle. */
public class VehicleDiagnosticTroubleCodes extends ApiData {
    private List<DiagnosticTroubleCode> activeCodes;

    /** No-argument constructor for deserialization. */
    public VehicleDiagnosticTroubleCodes() {
    }

    /** Returns the list of active diagnostic trouble codes. */
    public List<DiagnosticTroubleCode> getActiveCodes() {
        return this.activeCodes;
    }
}
