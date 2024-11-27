package com.smartcar.sdk.data;

/** POJO for Smartcar /battery/nominal_capacity endpoint */
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

  /**
   * Returns a stringified representation of the VehicleNominalCapacity object.
   *
   * @return A string describing the vehicle nominal capacity object.
   */
  @Override
  public String toString() {
      return this.getClass().getName() + "{" +
              "availableCapacities=" + Arrays.toString(this.availableCapacities) +
              ", capacity=" + this.capacity) +
              ", url=" + this.url +
              '}';
  }
}
