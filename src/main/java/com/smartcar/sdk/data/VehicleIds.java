package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /vehicles endpoint.
 */
public class VehicleIds extends ApiData {
  private String[] vehicleIds;

  /**
   * Initializes a new instance of VehicleIds
   *
   * @param vehicleIds the vehicleIds to be stored
   */
  public VehicleIds(final String[] vehicleIds) {
    this.vehicleIds = vehicleIds;
  }

  /**
   * Returns the vehicle IDs.
   *
   * @return the vehicle IDs
   */
  public String[] getVehicleIds() {
    return vehicleIds;
  }

  /**
   * Sets the vehicle IDs.
   *
   * @param vehicleIds the vehicle IDs
   */
  public void setVehicleIds(String[] vehicleIds) {
    this.vehicleIds = vehicleIds;
  }
}
