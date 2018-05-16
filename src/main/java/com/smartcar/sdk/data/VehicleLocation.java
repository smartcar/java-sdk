package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /location endpoint
 */
public class VehicleLocation extends ApiData {
  private int latitude;
  private int longitude;

  /**
   * Initializes a new instance of VehicleLocation
   *
   * @param latitude latitude of the vehicle
   * @param longitude longitude of the vehicle
   */
  public VehicleLocation(final int latitude, final int longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Returns the latitude of the vehicle
   *
   * @return latitude of the vehicle
   */
  public int getLatitude() {
    return this.latitude;
  }

  /**
   * Stores the latitude of the vehicle
   *
   * @param latitude latitude of the vehicle
   */
  public void setLatitude(int latitude) {
    this.latitude = latitude;
  }

  /**
   * Returns the longitude of the vehicle
   *
   * @return longitude of the vehicle
   */
  public int getLongitude() {
    return this.longitude;
  }

  /**
   * Stores the longitude of the vehicle
   *
   * @param longitude longitude of the vehicle
   */
  public void setLongitude(int longitude) {
    this.longitude = longitude;
  }
}
