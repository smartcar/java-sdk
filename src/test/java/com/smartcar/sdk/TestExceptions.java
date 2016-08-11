package com.smartcar.sdk;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.io.IOException;

public class TestExceptions {

  final String API_PATH = "/v1.0/vehicles";
  final String ACCESS_TOKEN = "access-token";
  final String VEHICLE_ID = "vehicle-id";

  Vehicle vehicle = new Vehicle(ACCESS_TOKEN, VEHICLE_ID);
  MockWebServer server;

  @BeforeMethod
  public void before(){
    server = new MockWebServer();
  }

  @AfterMethod
  public void after() throws IOException {
    server.shutdown();
  }

  private void setup(int code){
    server.enqueue(
      new MockResponse().setResponseCode(code)
    );
    try {
      server.start();
    } catch (IOException e) {
      System.out.println(e);
    }
    vehicle.setBaseUrl(server.url(API_PATH).toString());
  }

  @Test public void testValidationException(){
    setup(400);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e) {
      Assert.assertTrue(e instanceof Exceptions.ValidationException);
    }
  }
  @Test public void testAuthenticationException(){
    setup(401);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e) {
      Assert.assertTrue(e instanceof Exceptions.AuthenticationException);
    }
  }
  @Test public void testPermissionException(){
    setup(403);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e){
      Assert.assertTrue(e instanceof Exceptions.PermissionException);
    }
  }
  @Test public void testResourceNotFoundException(){
    setup(404);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e){
      Assert.assertTrue(e instanceof Exceptions.ResourceNotFoundException);
    }
  }
  @Test public void testStateException(){
    setup(409);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e) {
      Assert.assertTrue(e instanceof Exceptions.StateException);
    }
  }
  @Test public void testRateLimitingException(){
    setup(429);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e) {
      Assert.assertTrue(e instanceof Exceptions.RateLimitingException);
    }
  }
  @Test public void testMonthlyLimitExceededException(){
    setup(430);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e) {
      Assert.assertTrue(e instanceof Exceptions.MonthlyLimitExceeded);
    }
  }
  @Test public void testServerException(){
    setup(500);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e) {
      Assert.assertTrue(e instanceof Exceptions.ServerException);
    }
  }
  @Test public void testNotCapableException(){
    setup(501);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e) {
      Assert.assertTrue(e instanceof Exceptions.NotCapableException);
    }
  }
  @Test public void testGatewayTimeoutException(){
    setup(504);
    try {
      vehicle.info();
    } catch (Exceptions.SmartcarException e) {
      Assert.assertTrue(e instanceof Exceptions.GatewayTimeoutException);
    }
  }
}