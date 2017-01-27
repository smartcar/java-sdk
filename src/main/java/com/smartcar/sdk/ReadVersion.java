package com.smartcar.sdk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by robinjayaswal on 1/25/2017.
 */
public final class ReadVersion {
    static String result = "";
    static InputStream inputStream;
    public static String getVersionNumber() throws IOException {



        try {
            Properties prop = new Properties();
            String propFileName = "version.properties";

            inputStream = ReadVersion.class.getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {

                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            result = prop.getProperty("VERSION_BUILD");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }
}
