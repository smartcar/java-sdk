package com.smartcar.sdk;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test Suite: SmartcarException
 */
public class SmartcarExceptionTest {
  /**
   * Test throwing an SmartcarException with no parameters.
   */
  @Test
  public void testNewExceptionWithNoParams() {
    try {
      throw new SmartcarException();
    }
    catch (SmartcarException ex) {
      Assert.assertEquals(ex.getMessage(), SmartcarException.Status.UNKNOWN.toString());
      Assert.assertEquals(ex.getStatus(), SmartcarException.Status.UNKNOWN);
    }
  }

  /**
   * Test throwing an SmartcarException with a message.
   */
  @Test
  public void testNewExceptionWithMessage() {
    String testMessage = "Trysail Sail ho Corsair red ensign hulk smartly boom jib rum gangway.";

    try {
      throw new SmartcarException(testMessage);
    }
    catch (SmartcarException ex) {
      Assert.assertEquals(ex.getMessage(), testMessage);
      Assert.assertEquals(ex.getStatus(), SmartcarException.Status.UNKNOWN);
    }
  }

  /**
   * Test throwing an SmartcarException with a status.
   */
  @Test
  public void testNewExceptionWithStatus() {
    SmartcarException.Status testStatus = SmartcarException.Status.VALIDATION;

    try {
      throw new SmartcarException(testStatus);
    }
    catch (SmartcarException ex) {
      Assert.assertEquals(ex.getMessage(), testStatus.toString());
      Assert.assertEquals(ex.getStatus(), testStatus);
    }
  }

  /**
   * Test throwing an SmartcarException with both a message and a status.
   */
  @Test
  public void testNewExceptionWithMessageAndStatus() {
    String testMessage = "Case shot Shiver me timbers gangplank crack Jennys tea cup ballast.";
    SmartcarException.Status testStatus = SmartcarException.Status.AUTHENTICATION;

    try {
      throw new SmartcarException(testMessage, testStatus);
    }
    catch (SmartcarException ex) {
      Assert.assertEquals(ex.getMessage(), testMessage);
      Assert.assertEquals(ex.getStatus(), testStatus);
    }
  }

  /**
   * Test that the Status enum returns the expected status code.
   */
  @Test
  public void testGettingStatusCode() {
    SmartcarException.Status testStatus = SmartcarException.Status.PERMISSION;

    try {
      throw new SmartcarException(testStatus);
    }
    catch (SmartcarException ex) {
      Assert.assertEquals(ex.getStatus().getCode(), 403);
    }
  }

  /**
   * Test that the Status enum returns the expected status text.
   */
  @Test
  public void testGettingStatusText() {
    SmartcarException.Status testStatus = SmartcarException.Status.RESOURCE_NOT_FOUND;

    try {
      throw new SmartcarException(testStatus);
    }
    catch (SmartcarException ex) {
      Assert.assertEquals(ex.getStatus().getText(), "Resource Not Found");
    }
  }

  /**
   * Test that the correct Status is found given an integer code.
   */
  @Test
  public void testGettingKnownStatusForCode() {
    Assert.assertEquals(SmartcarException.Status.forCode(409), SmartcarException.Status.VEHICLE_STATE);
  }

  /**
   * Test that the UNKNOWN Status is found given a smarmy integer code.
   */
  @Test
  public void testGettingUnknownStatusForCode() {
    Assert.assertEquals(SmartcarException.Status.forCode(1337), SmartcarException.Status.UNKNOWN);
  }
}
