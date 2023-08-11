package com.smartcar.sdk.data;

/** POJO for the response paging w/cursor object */
public class ResponsePagingCursor extends ApiData {
    private int count;
    private String cursor;

    /**
     * Returns the response count
     *
     * @return response count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Returns the response cursor
     *
     * @return response cursor
     */
    public String getCursor() {
        return this.cursor;
    }

    /** @return a stringified representation of ResponsePagingCursor */
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "count=" + count + ", cursor=" + cursor + '}';
    }
}
