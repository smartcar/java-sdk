package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /battery endpoint
 */
public class VehicleBattery extends ApiData {
  private double range;
  private double percentRemaining;

  /**
   * Initializes a new instance of the VehicleBattery
   *
   * @param range The estimated remaining distance the car can travel
   *   (in kms or miles).
   * @param percentRemaining The remaining level of charge in the battery
   *   (in percent).
   */
  public VehicleBattery(final double range, final double percentRemaining) {
    this.range = range;
    this.percentRemaining = percentRemaining;
  }

  /**
   * Returns the battery range
   *
   * @return battery range
   */
  public double getRange() {
    return this.range;
  }

  /**
   * Returns the battery percent remaining
   *
   * @return battery percent remaining
   */
  public double getPercentRemaining() {
    return this.percentRemaining;
  }

  /**
   * Stores the battery range
   *
   * @param range stores the battery range
   */
  public void setRange(double range) {
    this.range = range;
  }

  /**
   * Stores the battery percent remaining
   *
   * @param percentRemaining stores the battery percent remaining
   */
  public void setPercentRemaining(double percentRemaining) {
    this.percentRemaining = percentRemaining;
  }

  /**
   * @return a stringified representation of VehicleBattery
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
            "range=" + range +
            "percentRemaining=" + percentRemaining +
            '}';
  }
}
