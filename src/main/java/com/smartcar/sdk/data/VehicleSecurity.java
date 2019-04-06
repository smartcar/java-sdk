package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /security endpoint
 */
public class VehicleSecurity extends ApiData {
  private String status;

  /**
   * Initializes a new instance of the VehicleSecurity
   *
   * @param status security request response
   */
  public VehicleSecurity(final String status) {
    this.status = status;
  }

  /**
   * Returns the security request response
   *
   * @return security request response
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * Stores the security request response
   *
   * @param status stores the security request response
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return a stringified representation of VehicleSecurity
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
            "status=" + status +
            '}';
  }
}
