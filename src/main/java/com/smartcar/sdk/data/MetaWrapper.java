package com.smartcar.sdk.data;

import java.util.Date;

/**
 * POJO for Wrapping ApiData with meta data
 */
public class MetaWrapper extends ApiData {
  private ApiData data;
  private String unitSystem;
  private Date age;

  /**
   * Initializes an instance of MetaWrapper
   *
   * @param data ApiData object
   * @param unitSystem unitSystem of the response
   * @param age age of the response
   */
  public MetaWrapper(final ApiData data, final String unitSystem, final Date age) {
    this.data = data;
    this.unitSystem = unitSystem;
    this.age = age;
  }

  /**
   * Return the response data
   *
   * @return response data
   */
  public ApiData getData() {
    return this.data;
  }

  /**
   * Stores the response data
   *
   * @param data response data
   */
  public void setData(ApiData data) {
    this.data = data;
  }

  /**
   * Return the unit system of the response
   *
   * @return the unit system of the response
   */
  public String getUnitSystem() {
    return this.unitSystem;
  }

  /**
   * Stores the unit system of the response
   *
   * @param unitSystem unit system of the response
   */
  public void setUnitSystem(String unitSystem) {
    this.unitSystem = unitSystem;
  }

  /**
   * Return the age of the response
   *
   * @return the age of the response
   */
  public Date getAge() {
    return this.age;
  }

  /**
   * Stores the age of the response
   */
  public void setAge(Date age) {
    this.age = age;
  }
}
