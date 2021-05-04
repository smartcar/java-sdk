package com.smartcar.sdk.data;

/** POJO for Smartcar /fuel endpoint */
public class VehicleFuel extends ApiData {
  private double range;
  private double percentRemaining;
  private double amountRemaining;

  /**
   * Initializes a new instance of the VehicleFuel
   *
   * @param range The estimated remaining distance the car can travel (in kms or miles). To set
   *     unit, {@link com.smartcar.sdk.Vehicle#setUnitSystem(Vehicle.UnitSystem)}.
   * @param percentRemaining The remaining level of fuel in the tank (in percent).
   * @param amountRemaining - The amount of fuel in the tank (in liters of gallons (US)). To set
   *     unit, {@link com.smartcar.sdk.Vehicle#setUnitSystem(Vehicle.UnitSystem)}.
   */
  public VehicleFuel(
      final double range, final double percentRemaining, final double amountRemaining) {
    this.range = range;
    this.percentRemaining = percentRemaining;
    this.amountRemaining = amountRemaining;
  }

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

  /**
   * Stores the fuel range
   *
   * @param range stores the fuel range
   */
  public void setRange(double range) {
    this.range = range;
  }

  /**
   * Stores the fuel percent remaining
   *
   * @param percentRemaining stores the fuel percent remaining
   */
  public void setPercentRemaining(double percentRemaining) {
    this.percentRemaining = percentRemaining;
  }

  /**
   * Stores the fuel amount remaining
   *
   * @param amountRemaining stores the fuel amount remaining
   */
  public void setAmountRemaining(double amountRemaining) {
    this.amountRemaining = amountRemaining;
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
