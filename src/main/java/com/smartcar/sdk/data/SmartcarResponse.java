package com.smartcar.sdk.data;

import java.util.Date;

/**
 * POJO for Wrapping ApiData with meta data
 */
public class SmartcarResponse<T extends ApiData> extends ApiData {
  private T data;
  private String unitSystem;
  private Date age;
  private ResponsePaging paging;

  /**
   * Initializes an instance of SmartcarResponse
   *
   * @param data Object extending the ApiData
   * @param unitSystem unitSystem of the response
   * @param age age of the response
   * @param paging the paging information included with the response
   */
  public SmartcarResponse(final T data, final String unitSystem, final Date age, final ResponsePaging paging) {
    this.data = data;
    this.unitSystem = unitSystem;
    this.age = age;
    this.paging = paging;
  }

  /**
   * Initializes an instance of SmartcarResponse
   *
   * @param data Object extending the ApiData
   * @param unitSystem unitSystem of the response
   * @param age age of the response
   */
  public SmartcarResponse(final T data, final String unitSystem, final Date age) {
    this(data, unitSystem, age, null);
  }

  /**
   * Initializes an instance of SmartcarResponse
   *
   * @param data Object extending the ApiData
   * @param paging the paging information included with the response
   */
  public SmartcarResponse(final T data, final ResponsePaging paging) {
    this(data, null, null, paging);
  }

  /**
  * Initializes an instance of SmartcarResponse
  *
  * @param data  Object extending the ApiData
  */
  public SmartcarResponse(final T data) {
    this(data, null, null);
  }

  /**
   * Return the response data
   *
   * @return response data
   */
  public T getData() {
    return this.data;
  }

  /**
   * Stores the response data
   *
   * @param data response data
   */
  public void setData(T data) {
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
