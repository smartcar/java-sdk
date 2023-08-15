package com.smartcar.sdk.data;

/** POJO for the response paging w/cursor object */
public class ResponsePagingCursor extends ApiData {
    private String cursor;

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
        return this.getClass().getName() + "{" + "cursor=" + cursor + '}';
    }
}
