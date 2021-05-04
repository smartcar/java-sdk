package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /vin endpoint
 */
public class VehicleVin extends ApiData {
  private String vin;

  /**
   * Initializes a new instance of VehicleVin
   *
   * @param vin vin of the vehicle
   */
  public VehicleVin(final String vin) {
    this.vin = vin;
  }

  /**
   * Returns the vehicle vin
   *
   * @return vehicle vin
   */
  public String getVin() {
    return this.vin;
  }

  /**
   * Stores the vehicle vin
   *
   * @param vin vin of the vehicle
   */
  public void setVin(String vin) {
    this.vin = vin;
  }

  /**
   * @return a stringified representation of VehicleVin
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "vin='" + vin + '\'' + '}';
  }
}
