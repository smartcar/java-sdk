package com.smartcar.sdk.data;

/** POJO for Smartcar /battery/capacity endpoint */
public class VehicleBatteryCapacity extends ApiData {
  private double capacity;

  /**
   * Returns the battery capacity
   *
   * @return battery capacity
   */
  public double getCapacity() {
    return this.capacity;
  }

  /** @return a stringified representation of VehicleBattery */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "capacity=" + capacity + '}';
  }
}
