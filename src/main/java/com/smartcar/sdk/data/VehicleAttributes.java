package com.smartcar.sdk.data;

/** POJO for Smartcar /attributes endpoint */
public class VehicleAttributes extends ApiData {
  private String id;
  private String make;
  private String model;
  private int year;

  /**
   * Returns the vehicle id.
   *
   * @return vehicle id
   */
  public String getId() {
    return this.id;
  }

  /**
   * Returns the vehicle make
   *
   * @return vehicle make
   */
  public String getMake() {
    return this.make;
  }

  /**
   * Returns the vehicle model
   *
   * @return model of the vehicle
   */
  public String getModel() {
    return this.model;
  }

  /**
   * Returns the year of the vehicle
   *
   * @return year of the vehicle
   */
  public int getYear() {
    return this.year;
  }

  /** @return a stringified representation of VehicleInfo */
  @Override
  public String toString() {
    return this.getClass().getName()
        + "{"
        + "id='"
        + id
        + '\''
        + ", make='"
        + make
        + '\''
        + ", model='"
        + model
        + '\''
        + ", year="
        + year
        + '}';
  }
}
