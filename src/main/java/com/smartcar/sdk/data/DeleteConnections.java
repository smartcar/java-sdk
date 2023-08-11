package com.smartcar.sdk.data;

import java.util.Arrays;

public class DeleteConnections extends ApiData {
    private Connection[] connections;

    public DeleteConnections(final Connection[] connections) {
        this.connections = connections;
    }

    public Connection[] getConnections() {
        return connections;
    }

    @Override public String toString() {
        return "DeleteConnections{" +
                "connections=" + Arrays.toString(connections) +
                '}';
    }
}
