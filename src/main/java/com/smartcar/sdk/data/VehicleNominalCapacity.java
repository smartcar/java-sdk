package com.smartcar.sdk.data;

import java.util.List;

/** Data class for Smartcar /battery/nominal_capacity endpoint */
public class VehicleNominalCapacity extends ApiData {
  private List<AvailableCapacity> availableCapacities;
  private SelectedCapacity capacity;
  private String url;

  /** Returns the list of available capacites. */
  public List<AvailableCapacity> getAvailableCapacities() {
    return this.availableCapacities;
  }
  
  /** Returns the selected capacity. */
  public SelectedCapacity getCapacity() {
    return this.capacity;
  }
  
  /** Returns the url. */
  public String getUrl() {
    return this.url;
  }
}
