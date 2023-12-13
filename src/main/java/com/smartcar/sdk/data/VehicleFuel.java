package com.smartcar.sdk.data;

/** POJO for Smartcar /fuel endpoint */
public class VehicleFuel extends ApiData {
  private Double range;
  private Double percentRemaining;
  private Double amountRemaining;

  /**
   * Returns the fuel range
   *
   * @return fuel range
   */
  public Double getRange() {
    return this.range;
  }

  /**
   * Returns the fuel percent remaining
   *
   * @return fuel percent remaining
   */
  public Double getPercentRemaining() {
    return this.percentRemaining;
  }

  /**
   * Returns the fuel amount remaining
   *
   * @return fuel amount remaining
   */
  public Double getAmountRemaining() {
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

