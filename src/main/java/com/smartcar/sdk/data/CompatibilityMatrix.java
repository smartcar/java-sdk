package com.smartcar.sdk.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/** A container for the compatibility matrix endpoint */
public class CompatibilityMatrix extends ApiData {
  private Map<String, List<CompatibilityEntry>> makeCompatibilityMap;

  public class CompatibilityEntry extends ApiData {
    private String model;
    private String startYear;
    private String endYear;
    private String type;
    private String[] endpoints;
    private String[] permissions;

    /**
     * Returns the model
     * @return model
     */
    public String getModel() {
      return model;
    }

    /**
     * Returns the start year
     * @return startYear
     */
    public String getStartYear() {
      return startYear;
    }

    /**
     * Returns the end year
     * @return endYear
     */
    public String getEndYear() {
      return endYear;
    }

    /**
     * Returns the type
     * @return type
     */
    public String getType() {
      return type;
    }

    /**
     * Returns the endpoints
     * @return endpoints
     */
    public String[] getEndpoints() {
      return endpoints;
    }

    /**
     * Returns the permissions
     * @return permissions
     */
    public String[] getPermissions() {
      return permissions;
    }

    /**
     * Return the string representation
     *
     * @return String representation
     */
    @Override
    public String toString() {
      return "CompatibilityEntry{" +
              "model='" + model + '\'' +
              ", startYear='" + startYear + '\'' +
              ", endYear='" + endYear + '\'' +
              ", type='" + type + '\'' +
              ", endpoints=" + String.join(",", endpoints) +
              ", permissions=" + String.join(",", permissions) +
              '}';
    }
  }

  /**
   * Returns the make compatibility map
   * @return makeCompatibilityMap
   */
  public Map<String, List<CompatibilityEntry>> getResults() {
    if (makeCompatibilityMap == null) {
      return Collections.emptyMap();
    }
    return makeCompatibilityMap;
  }

  /**
   * Return the string representation
   *
   * @return String representation
   */
  @Override
  public String toString() {
    return "CompatibilityMatrix{" +
            "makeCompatibilityMap=" + makeCompatibilityMap +
            '}';
  }
}
