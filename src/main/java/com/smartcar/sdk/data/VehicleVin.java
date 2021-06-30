package com.smartcar.sdk.data;

/** POJO for Smartcar /vin endpoint */
public class VehicleVin extends ApiData {
  private String vin;

  /**
   * Returns the vehicle vin
   *
   * @return vehicle vin
   */
  public String getVin() {
    return this.vin;
  }

  /** @return a stringified representation of VehicleVin */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "vin='" + vin + '\'' + '}';
  }
}
