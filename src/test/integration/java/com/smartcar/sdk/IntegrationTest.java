package com.smartcar.sdk;

import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class IntegrationTest {
  public enum Environment {
    DEV,
    QA,
    STAGE,
    PROD;
  }

  /**
   * An extension of Properties that supports automatic environment variable
   * expansion.
   */
  protected static class EnvProperties extends Properties {
    /**
     * Reads propertys from the input byte stream.
     *
     * @param inStream the input stream
     *
     * @throws IOException if an error occurred when reading the input stream
     */
    public synchronized void load(InputStream inStream) throws IOException {
      // Load Properties
      super.load(inStream);

      // Expand Environment Variables
      Set<String> stringKeys = this.stringPropertyNames();

      for(String key : stringKeys) {
        this.setProperty(key, EnvProperties.expandEnvVars(this.getProperty(key)));
      }
    }

    /**
     * Expand any environment variables embedded in the specified value.
     *
     * @param input the string containing possible environment variables
     *
     * @return the input with environment variable references expanded
     */
    private static String expandEnvVars(String input) {
      if(input == null) {
        return null;
      }

      Matcher matcher = Pattern.compile("\\$(\\{(\\w+)}|(\\w+))").matcher(input);
      StringBuffer buffer = new StringBuffer();

      while(matcher.find()) {
        String envVarName = matcher.group(2) == null ? matcher.group(3) : matcher.group(2);
        String envVarValue = System.getenv(envVarName);

        matcher.appendReplacement(buffer, envVarValue == null ? "" : envVarValue);
      }

      matcher.appendTail(buffer);

      return buffer.toString();
    }
  }

  protected EnvProperties config = new EnvProperties();

  /**
   * Loads the configuration for the specified environment.
   *
   * @param env the desired environment
   */
  private void loadConfig(Environment env) throws IOException {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream(
        "environment." + env.toString().toLowerCase() + ".properties");
    this.config.load(stream);
    stream.close();
  }

  /**
   * Loads configuration before executing tests.
   *
   * @throws IOException if the configuration cannot be loaded
   */
  @BeforeClass
  public void beforeClass() throws IOException {
    String targetEnvString = System.getenv("INTEGRATION_TARGET_ENV");
    Environment targetEnv;

    if(targetEnvString == null) {
      targetEnv = Environment.QA;
    }
    else {
      targetEnv = Environment.valueOf(targetEnvString);
    }

    this.loadConfig(targetEnv);
  }
}
