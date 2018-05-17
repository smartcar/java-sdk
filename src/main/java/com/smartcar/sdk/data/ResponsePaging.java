package com.smartcar.sdk.data;

/**
 * POJO for the paging object
 */
public class ResponsePaging extends ApiData {
  private int count;
  private int offset;

  /**
   * Initializes a new instance of ResponsePaging
   *
   * @param count the number of items in the response
   * @param offset the index to start the response list
   */
  public ResponsePaging(final int count, final int offset) {
    this.count = count;
    this.offset = offset;
  }

  /**
   * Returns the response count
   *
   * @return response count
   */
  public int getCount() {
    return this.count;
  }

  /**
   * Stores the response count
   *
   * @param count response count
   */
  public void setCount(int count) {
    this.count = count;
  }

  /**
   * Returns the response offset
   *
   * @return response offset
   */
  public int getOffset() {
    return this.offset;
  }

  /**
   * Stores the response offset
   *
   * @param offset response offset
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }
}
