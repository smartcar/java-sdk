package com.smartcar.sdk;

import java.io.IOException;
import okhttp3.mockwebserver.MockWebServer;
import org.testng.IExecutionListener;

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
