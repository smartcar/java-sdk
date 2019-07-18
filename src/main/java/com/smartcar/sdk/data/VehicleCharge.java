package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /charge endpoint
 */
public class VehicleCharge extends ApiData {
  private String state;
  private boolean isPluggedIn;

  /**
   * Initializes a new instance of the VehicleCharge
   *
   * @param state Indicates the current state of the charge system. Can be
   *   `FULLY_CHARGED`, `CHARGING`, or `NOT_CHARGING`.
   * @param isPluggedIn Indicates whether charging cable is plugged in.
   */
  public VehicleCharge(final String state, final boolean isPluggedIn) {
    this.state = state;
    this.isPluggedIn = isPluggedIn;
  }

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

  /**
   * Stores whether charging cable is plugged in
   *
   * @param isPluggedIn stores whether charging cable is plugged in
   */
  public void setIsPluggedIn(boolean isPluggedIn) {
    this.isPluggedIn = isPluggedIn;
  }

  /**
   * @return a stringified representation of VehicleCharge
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
            "state=" + state +
            "isPluggedIn=" + isPluggedIn +
            '}';
  }
}
