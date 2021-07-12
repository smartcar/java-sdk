package com.smartcar.sdk.data;

/** POJO for Smartcar /odometer endpoint */
public class VehicleOdometer extends ApiData {
  private double distance;

  /**
   * Returns the odometer distance
   *
   * @return odometer distance
   */
  public double getDistance() {
    return this.distance;
  }

  /** @return a stringified representation of VehicleOdometer */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "distance=" + distance + '}';
  }
}
