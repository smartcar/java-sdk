package com.smartcar.sdk.data;

/** POJO for Smartcar /location endpodouble */
public class VehicleLocation extends ApiData {
  private double latitude;
  private double longitude;

  /**
   * Returns the latitude of the vehicle
   *
   * @return latitude of the vehicle
   */
  public double getLatitude() {
    return this.latitude;
  }

  /**
   * Stores the latitude of the vehicle
   *
   * @param latitude latitude of the vehicle
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  /**
   * Returns the longitude of the vehicle
   *
   * @return longitude of the vehicle
   */
  public double getLongitude() {
    return this.longitude;
  }

  /** @return a stringified representation of VehicleLocation */
  @Override
  public String toString() {
    return this.getClass().getName()
        + "{"
        + "latitude="
        + latitude
        + ", longitude="
        + longitude
        + '}';
  }
}
