package com.smartcar.sdk.data;

import java.util.Date;

/**
 * POJO for Wrapping ApiData with meta data
 */
public class SmartcarResponse<T extends ApiData> extends ApiData {
  private T data;
  private String unitSystem;
  private Date age;
  private String requestId;
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
   *
   * @param age the age of the response
   */
  public void setAge(Date age) {
    this.age = age;
  }

  /**
   * Return the Smartcar request id from the response headers
   *
   * @return the request id
   */
  public String getRequestId() {
    return this.requestId;
  }

  /**
   * Stores the age of the response
   *
   * @param requestId the request id
   */
  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  /**
   * Return paging information
   *
   * @return the paging information
   */
  public ResponsePaging getPaging() {
    return this.paging;
  }

  /**
   * Stores the paging information
   *
   * @param paging the paging information
   */
  public void setPaging(ResponsePaging paging) {
    this.paging = paging;
  }

  /**
   * @return a stringified representation of SmartcarResponse
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
            "data=" + data +
            ", unitSystem='" + unitSystem + '\'' +
            ", age=" + age +
            ", requestId=" + requestId +
            ", paging=" + paging +
            '}';
  }
}
