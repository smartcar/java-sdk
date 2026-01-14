package com.smartcar.sdk;

import okhttp3.mockwebserver.MockWebServer;
import org.testng.IExecutionListener;

import java.io.IOException;

public class TestExecutionListener implements IExecutionListener {
    static MockWebServer mockWebServer = new MockWebServer();

    @Override
    public void onExecutionStart() {
        System.out.println("STARTING....");
        try {
            mockWebServer.start(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Smartcar.API_ORIGIN = "http://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort();
        Smartcar.API_V3_ORIGIN = "http://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort();
    }

    @Override
    public void onExecutionFinish() {
        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
