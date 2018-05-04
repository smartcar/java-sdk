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
      Assert.assertNull(ex.getMessage());
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
      Assert.assertNull(ex.getMessage());
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
}
