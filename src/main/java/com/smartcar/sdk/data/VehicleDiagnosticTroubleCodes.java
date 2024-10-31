package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Data class for the diagnostic trouble codes of a vehicle. */
public class VehicleDiagnosticTroubleCodes extends ApiData {
    @SerializedName("activeCodes")
    private List<DiagnosticTroubleCode> activeCodes;

    /** No-argument constructor for deserialization. */
    public VehicleDiagnosticTroubleCodes() {
    }

    /** Returns the list of active diagnostic trouble codes. */
    public List<DiagnosticTroubleCode> getActiveCodes() {
        return activeCodes;
    }

    /** Sets the list of active diagnostic trouble codes. */
    public void setActiveCodes(List<DiagnosticTroubleCode> activeCodes) {
        this.activeCodes = activeCodes;
    }
}
