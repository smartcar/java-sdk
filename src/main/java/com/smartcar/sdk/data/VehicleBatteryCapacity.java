package com.smartcar.sdk.data;

/** POJO for Smartcar /battery/capacity endpoint */
public class VehicleBatteryCapacity extends ApiData {
  private double capacity;

  /**
   * Initializes a new instance of the VehicleBatteryCapacity
   *
   * @param capacity The battery capacity of the vehicle (in kWh).
   */
  public VehicleBatteryCapacity(final double capacity) {
    this.capacity = capacity;
  }

  /**
   * Returns the battery capacity
   *
   * @return battery capacity
   */
  public double getCapacity() {
    return this.capacity;
  }

  /**
   * Stores the battery capacity
   *
   * @param capacity stores the battery capacity
   */
  public void setCapacity(double capacity) {
    this.capacity = capacity;
  }

  /** @return a stringified representation of VehicleBattery */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "capacity=" + capacity + '}';
  }
}
