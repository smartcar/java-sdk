package com.smartcar.sdk.data;

/** POJO for Smartcar /odometer endpoint */
public class VehicleOdometer extends ApiData {
  private double distance;

  /**
   * Initializes a new instance of the VehicleOdometer
   *
   * @param distance odometer distance
   */
  public VehicleOdometer(final double distance) {
    this.distance = distance;
  }

  /**
   * Returns the odometer distance
   *
   * @return odometer distance
   */
  public double getDistance() {
    return this.distance;
  }

  /**
   * Stores the odomter distance
   *
   * @param distance stores the odometer distance
   */
  public void setDistance(double distance) {
    this.distance = distance;
  }

  /** @return a stringified representation of VehicleOdometer */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "distance=" + distance + '}';
  }
}
