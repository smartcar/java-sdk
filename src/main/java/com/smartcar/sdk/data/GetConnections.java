package com.smartcar.sdk.data;

import java.util.Arrays;

public class GetConnections extends ApiData {
    private Connection[] connections;

    private ResponsePagingCursor paging;
    public GetConnections(final Connection[] connections, final ResponsePagingCursor paging) {
        this.connections = connections;
        this.paging = paging;
    }

    public Connection[] getConnections() {
        return connections;
    }

    public ResponsePagingCursor getPaging() {
        return paging;
    }

    @Override
    public String toString() {
        return "GetConnections{" +
                "connections=" + Arrays.toString(connections) +
                ", paging=" + paging +
                '}';
    }
}
