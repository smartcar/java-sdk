package com.smartcar.sdk.data;

/** POJO for Smartcar /engine/oil endpoint */
public class VehicleEngineOil extends ApiData {
  private double lifeRemaining;

  /**
   * Returns the oil life remaining
   *
   * @return oil life remaining
   */
  public double getLifeRemaining() {
    return this.lifeRemaining;
  }

  /** @return a stringified representation of VehicleFuel */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "lifeRemaining=" + lifeRemaining + '}';
  }
}
