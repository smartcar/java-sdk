package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /location endpoint
 */
public class VehicleLocation extends ApiData {
  private Int latitude;
  private Int longitude;

  /**
   * Initializes a new instance of VehicleLocation
   *
   * @param latitude latitude of the vehicle
   * @param longitude longitude of the vehicle
   */
  public VehicleLocation(final Int latitude, final Int longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Returns the latitude of the vehicle
   *
   * @return latitude of the vehicle
   */
  public Int getLatitude() {
    return this.latitude;
  }

  /**
   * Stores the latitude of the vehicle
   *
   * @param latitude latitude of the vehicle
   */
  public void setLatitude(Int latitude) {
    this.latitude = latitude;
  }

  /**
   * Returns the longitude of the vehicle
   *
   * @return longitude of the vehicle
   */
  public Int getLongitude() {
    return this.longitude;
  }

  /**
   * Stores the longitude of the vehicle
   *
   * @param longitude longitude of the vehicle
   */
  public void setLongitude(Int longitude) {
    this.longitude = longitude;
  }
}
