package com.smartcar.sdk.data;

/** POJO for Smartcar /battery endpoint */
public class VehicleBattery extends ApiData {
  private double range;
  private double percentRemaining;

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

  /** @return a stringified representation of VehicleBattery */
  @Override
  public String toString() {
    return this.getClass().getName()
        + "{"
        + "range="
        + range
        + "percentRemaining="
        + percentRemaining
        + '}';
  }
}
