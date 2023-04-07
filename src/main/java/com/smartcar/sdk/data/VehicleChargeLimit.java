package com.smartcar.sdk.data;

public class VehicleChargeLimit extends ApiData {
    private double limit;

  /**
   * Returns whether charging cable is plugged in
   *
   * @return whether charging cable is plugged in
   */
  public boolean getChargeLimit() {
    return this.limit;
  }

  // TODO: add .toString()
  /** @return a stringified representation of VehicleFuel */
  @Override
  public String toString() {
    return this.getClass().getChargeLimit()
        + "{"
        + "limit="
        + limit
        + '}';
  }
}