package com.smartcar.sdk.data;

import java.util.Arrays;

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

  /**
   * @return a stringified representation of VehicleIds
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
            "vehicleIds=" + Arrays.toString(vehicleIds) +
            '}';
  }
}
