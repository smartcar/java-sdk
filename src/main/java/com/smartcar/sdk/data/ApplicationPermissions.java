package com.smartcar.sdk.data;

/**
 * POJO for Smartcar /permissions endpoint
 */
public class ApplicationPermissions extends ApiData {
  private ResponsePaging paging;
  private String[] permissions;

  /**
   * Initializes a new instance of ApplicationPermission
   *
   * @param paging metadata about the list of elements
   * @param permissions an array of permissions
   */
  public ApplicationPermissions(final ResponsePaging paging, final String[] permissions) {
    this.paging = paging;
    this.permissions = permissions;
  }

  /**
   * Returns the paging information of the response
   *
   * @return paging information of the response
   */
  public ResponsePaging getPaging() {
    return this.paging;
  }

  /**
   * Stores the paging information of the response
   *
   * @param paging information of the response
   */
  public void setPaging(ResponsePaging paging) {
    this.paging = paging;
  }

  /**
   * Returns the application permissions
   *
   * @return application permissions
   */
  public String[] getPermissions() {
    return this.permissions;
  }

  /**
   * Stores the application permissions
   *
   * @param permissions application permissions
   */
  public void setPermissions(String[] permissions) {
    this.permissions = permissions;
  }
}
