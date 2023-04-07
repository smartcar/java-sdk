package com.smartcar.sdk.data;

/** POJO for Smartcar /fuel endpoint */
public class VehicleFuel extends ApiData {
  private double range;
  private double percentRemaining;
  private double amountRemaining;

  /**
   * Returns the fuel range
   *
   * @return fuel range
   */
  public double getRange() {
    return this.range;
  }

  /**
   * Returns the fuel percent remaining
   *
   * @return fuel percent remaining
   */
  public double getPercentRemaining() {
    return this.percentRemaining;
  }

  /**
   * Returns the fuel amount remaining
   *
   * @return fuel amount remaining
   */
  public double getAmountRemaining() {
    return this.amountRemaining;
  }

  /** @return a stringified representation of VehicleFuel */
  @Override
  public String toString() {
    return this.getClass().getName()
        + "{"
        + "range="
        + range
        + "percentRemaining="
        + percentRemaining
        + "amountRemaining="
        + amountRemaining
        + '}';
  }
}

