package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /engine/oil endpoint
 */
public class VehicleOil extends ApiData {
  private double lifeRemaining;

  /**
   * Initializes a new instance of VehicleOil
   *
   * @param lifeRemaining the engine oil's remaining life span (as a percentage).
   */
  public VehicleOil(double lifeRemaining) {
    this.lifeRemaining = lifeRemaining;
  }

  /**
   * Returns the oil life remaining
   *
   * @return oil life remaining
   */
  public double getLifeRemaining() {
    return this.lifeRemaining;
  }

  /**
   * Stores the oil life remaining
   *
   * @param lifeRemaining stores the oil life remaining
   */
  public void setLifeRemaining(double lifeRemaining) {
    this.lifeRemaining = lifeRemaining;
  }

  /**
   * @return a stringified representation of VehicleFuel
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
            "lifeRemaining=" + lifeRemaining +
            '}';
  }
}
