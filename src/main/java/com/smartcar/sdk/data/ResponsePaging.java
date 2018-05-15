package com.smartcar.sdk.data;

/**
 * POJO for the paging object
 */
public class ResponsePaging extends ApiData {
  private Int count;
  private Int offset;

  /**
   * Initializes a new instance of ResponsePaging
   *
   * @param count the number of items in the response
   * @param offset the index to start the response list
   */
  public ResponsePaging(final Int count, final Int offset) {
    this.count = count;
    this.offset = offset;
  }

  /**
   * Returns the response count
   *
   * @return response count
   */
  public Int getCount() {
    return this.count;
  }

  /**
   * Stores the response count
   *
   * @param count response count
   */
  public void setCount(Int count) {
    this.count = count;
  }

  /**
   * Returns the response offset
   *
   * @return response offset
   */
  public Int getOffset() {
    return this.offset;
  }

  /**
   * Stores the response offset
   *
   * @param offset response offset
   */
  public void setOffset(Int offset) {
    this.offset = offset;
  }
}
