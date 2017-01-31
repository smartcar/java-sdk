package com.smartcar.sdk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * ReadVersion is used by the request methods in the sdk to attach the correct
 */
public final class ReadVersion {
  static String result = "";
  static InputStream inputStream;
  public static String getVersionNumber() {
    try {
      Properties prop = new Properties();
      String propFileName = "version.properties";

      inputStream = ReadVersion.class.getClassLoader().getResourceAsStream(propFileName);

      prop.load(inputStream);

      result = prop.getProperty("VERSION");

      inputStream.close();
    } catch (Exception e) {
      result = "0.0.0";
    }

    return result;
  }
}
