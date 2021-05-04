package com.smartcar.sdk;

import org.testng.Assert;
import org.testng.annotations.Test;

/** Test Suite: Utils */
public class UtilsTest {
  /** Verifies that join combines all array elements, delimited by the specified separator. */
  @Test
  public void testJoin() {
    String separator = ", ";
    String[] data = {"unus", "duo", "tres", "quattor"};

    String expected = "unus, duo, tres, quattor";

    Assert.assertEquals(Utils.join(data, separator), expected);
  }

  /**
   * Verifies that joining a single element results in only that element as a string with no usages
   * of the specified separator.
   */
  @Test
  public void testJoinWithOneElement() {
    String separator = "::";
    String[] data = {"qinque"};

    String expected = "qinque";

    Assert.assertEquals(Utils.join(data, separator), expected);
  }

  /** Verifies that joining an empty array results in an empty string. */
  @Test
  public void testJoinWithEmptyArray() {
    String separator = "-";
    String[] data = {};
    String expected = "";

    Assert.assertEquals(Utils.join(data, separator), expected);
  }
}
