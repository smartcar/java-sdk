package com.smartcar.sdk;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestUtil {

  @Test
  public void testJoinWithMultiArray(){

    Assert.assertEquals(
      Util.join(new String[] {"one", "two", "three"}),
      "one two three"
    );
  }

  @Test
  public void testJoinWithSingularArray(){
    Assert.assertEquals(
      Util.join(new String[]{"one"}),
      "one"
    );
  }

  @Test
  public void testJoinWithEmptyArray(){

    Assert.assertEquals(
      Util.join(new String[]{}),
      ""
    );
  }
}