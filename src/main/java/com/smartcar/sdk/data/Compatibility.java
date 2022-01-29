package com.smartcar.sdk.data;

import java.util.Arrays;

/** A container for the compatibility endpoint */
public class Compatibility extends ApiData {
  private boolean compatible;
  private String reason;
  private Capability[] capabilities;

  /**
   * Return the compatible variable
   *
   * @return compatible
   */
  public boolean getCompatible() {
    return this.compatible;
  }

  /**
   * Return the reason variable
   *
   * @return reason
   */
  public String getReason() {
    return this.reason;
  }

  /**
   * Returns the capabilities
   *
   * @return capabilities
   */
  public Capability[] getCapabilities() {
    return this.capabilities;
  }

  /**
   * Return the string representation
   *
   * @return String representation
   */
  @Override
  public String toString() {
    return "Compatibility{"
        + "compatible="
        + compatible
        + ", reason='"
        + reason
        + '\''
        + ", capabilities="
        + Arrays.toString(capabilities)
        + '}';
  }

  public static class Capability extends ApiData {
    private boolean capable;
    private String permission;
    private String endpoint;
    private String reason;

    /**
     * Returns capable field
     *
     * @return capable
     */
    public boolean getCapable() {
      return capable;
    }

    /**
     * Returns permission field
     *
     * @return permission
     */
    public String getPermission() {
      return permission;
    }

    /**
     * Returns endpoint field
     *
     * @return endpoint
     */
    public String getEndpoint() {
      return endpoint;
    }

    /**
     * Returns reason field
     *
     * @return reason
     */
    public String getReason() {
      return reason;
    }

    /**
     * Return the string representation
     *
     * @return String representation
     */
    @Override
    public String toString() {
      return "Capability{"
          + "capable="
          + capable
          + ", permission='"
          + permission
          + '\''
          + ", endpoint='"
          + endpoint
          + '\''
          + ", reason='"
          + reason
          + '\''
          + '}';
    }
  }
}
