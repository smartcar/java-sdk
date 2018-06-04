package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /info endpoint
 */
public class VehicleInfo extends ApiData {
  private String id;
  private String make;
  private String model;
  private int year;

  /**
   * Initializes a new instance of VehicleInfo
   *
   * @param id uuid of the vehicle
   * @param make make of the vehicle
   * @param model model of the vehicle
   * @param year year of the vehicle
   */
  public VehicleInfo(final String id, final String make, final String model, final int year) {
     this.id = id;
     this.make = make;
     this.model = model;
     this.year = year;
  }

  /**
   * Returns the vehicle id.
   *
   * @return vehicle id
   */
  public String getId() {
    return this.id;
  }

  /**
   * Stores the vehicle id
   *
   * @param id the vehicle id
   */
  public void setId(String id) {
    this.id = id;
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
   * Stores the vehicle make
   *
   * @param make make of the vehicle
   */
  public void setMake(String make) {
    this.make = make;
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
   * Stores the vehicle model
   *
   * @param model model of the vehicle
   */
  public void setModel(String model) {
    this.model = model;
  }

  /**
   * Returns the year of the vehicle
   *
   * @return year of the vehicle
   */
  public int getYear() {
    return this.year;
  }

  /**
   * Stores the year of the vehicle
   *
   * @param year year of the vehicle
   */
  public void setYear(int year) {
    this.year = year;
  }
}
