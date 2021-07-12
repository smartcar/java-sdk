package com.smartcar.sdk.data;

/** POJO for the paging object */
public class RequestPaging extends ApiData {
  private int limit;
  private int offset;

  /**
   * Initializes a new instance of RequestPaging
   *
   * @param limit the maximum number of items in the response
   * @param offset the index to start the response list
   */
  public RequestPaging(final int limit, final int offset) {
    this.limit = limit;
    this.offset = offset;
  }

  /**
   * Returns the response limit
   *
   * @return response limit
   */
  public int getLimit() {
    return this.limit;
  }

  /**
   * Returns the response offset
   *
   * @return response offset
   */
  public int getOffset() {
    return this.offset;
  }

  /** @return a stringified representation of RequestPaging */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "limit=" + limit + ", offset=" + offset + '}';
  }
}
