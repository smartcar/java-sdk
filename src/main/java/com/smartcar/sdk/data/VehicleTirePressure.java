package com.smartcar.sdk.data;

/** POJO for Smartcar /tires/pressure endpoint */
public class VehicleTirePressure extends ApiData {
  private double frontLeft;
  private double frontRight;
  private double backLeft;
  private double backRight;

  /**
   * Initializes a new instance of the VehicleTirePressure
   *
   * @param frontLeft The current air pressure of the front left tire (in psi or kpa). To set unit,
   *     {@link com.smartcar.sdk.Vehicle#setUnitSystem(Vehicle.UnitSystem)}.
   * @param frontRight The current air pressure of the front right tire (in psi or kpa). To set
   *     unit, {@link com.smartcar.sdk.Vehicle#setUnitSystem(Vehicle.UnitSystem)}.
   * @param backLeft The current air pressure of the back left tire (in psi or kpa). To set unit,
   *     {@link com.smartcar.sdk.Vehicle#setUnitSystem(Vehicle.UnitSystem)}.
   * @param backRight The current air pressure of the back right tire (in psi or kpa). To set unit,
   *     {@link com.smartcar.sdk.Vehicle#setUnitSystem(Vehicle.UnitSystem)}.
   */
  public VehicleTirePressure(
      final double frontLeft,
      final double frontRight,
      final double backLeft,
      final double backRight) {
    this.frontLeft = frontLeft;
    this.frontRight = frontRight;
    this.backLeft = backLeft;
    this.backRight = backRight;
  }

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

  /**
   * Stores the front left tire pressure
   *
   * @param frontLeft stores the front left tire pressure
   */
  public void setFrontLeft(double frontLeft) {
    this.frontLeft = frontLeft;
  }

  /**
   * Stores the front right tire pressure
   *
   * @param frontRight stores the front right tire pressure
   */
  public void setFrontRight(double frontRight) {
    this.frontRight = frontRight;
  }

  /**
   * Stores the back left tire pressure
   *
   * @param backLeft stores the back left tire pressure
   */
  public void setBackLeft(double backLeft) {
    this.backLeft = backLeft;
  }

  /**
   * Stores the back right tire pressure
   *
   * @param backRight stores the back right tire pressure
   */
  public void setBackRight(double backRight) {
    this.backRight = backRight;
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
