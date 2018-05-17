package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /odometer endpoint
 */
public class VehicleOdometer extends ApiData {
  private int distance;

  /**
   * Initializes a new instance of the VehicleOdometer
   *
   * @param distance odometer distance
   */
  public VehicleOdometer(final int distance) {
    this.distance = distance;
  }

  /**
   * Returns the odometer distance
   *
   * @return odometer distance
   */
  public int getDistance() {
    return this.distance;
  }

  /**
   * Stores the odomter distance
   *
   * @param distance stores the odometer distance
   */
  public void setDistance(int distance) {
    this.distance = distance;
  }
}
