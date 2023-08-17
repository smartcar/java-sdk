package com.smartcar.sdk.data;

/** Builder class for setting request paging w/cursor options */
public class RequestPagingCursor {
    private Integer limit;
    private String cursor;

    private RequestPagingCursor(Builder builder) {
        this.limit = builder.limit;
        this.cursor = builder.cursor;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getCursor() {
        return cursor;
    }

    public static class Builder {
        private Integer limit;
        private String cursor;

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        public RequestPagingCursor build() {
            return new RequestPagingCursor(this);
        }
    }

    @Override
    public String toString() {
        return "RequestPagingCursor{" +
                "limit=" + limit +
                ", cursor='" + cursor + '\'' +
                '}';
    }
}
