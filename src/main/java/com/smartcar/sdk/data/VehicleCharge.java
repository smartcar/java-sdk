package com.smartcar.sdk.data;

/** POJO for Smartcar /charge endpoint */
public class VehicleCharge extends ApiData {
  private String state;
  private boolean isPluggedIn;

  /**
   * Returns the charge state
   *
   * @return charge state
   */
  public String getState() {
    return this.state;
  }

  /**
   * Returns whether charging cable is plugged in
   *
   * @return whether charging cable is plugged in
   */
  public boolean getIsPluggedIn() {
    return this.isPluggedIn;
  }

  /**
   * Stores the charge state
   *
   * @param state stores the charge state
   */
  public void setState(String state) {
    this.state = state;
  }

  /** @return a stringified representation of VehicleCharge */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "state=" + state + "isPluggedIn=" + isPluggedIn + '}';
  }
}
