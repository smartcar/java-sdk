package com.smartcar.sdk.data;

/** POJO for Smartcar /tires/pressure endpoint */
public class VehicleTirePressure extends ApiData {
  private double frontLeft;
  private double frontRight;
  private double backLeft;
  private double backRight;

  /**
   * Returns the front left tire pressure
   *
   * @return front left tire pressure
   */
  public double getFrontLeft() {
    return this.frontLeft;
  }

  /**
   * Returns the front right tire pressure
   *
   * @return front right tire pressure
   */
  public double getFrontRight() {
    return this.frontRight;
  }

  /**
   * Returns the back left tire pressure
   *
   * @return back left tire pressure
   */
  public double getBackLeft() {
    return this.backLeft;
  }

  /**
   * Returns the back right tire pressure
   *
   * @return backRight right tire pressure
   */
  public double getBackRight() {
    return this.backRight;
  }

  /** @return a stringified representation of VehicleTirePressure */
  @Override
  public String toString() {
    return this.getClass().getName()
        + "{"
        + "frontLeft="
        + frontLeft
        + "frontRight="
        + frontRight
        + "backLeft="
        + backLeft
        + "backRight="
        + backRight
        + '}';
  }
}
