package com.smartcar.sdk.data;

/** A container for the compatibility endpoint */
public class Compatibility extends ApiData {
  private boolean compatible;

  /**
   * Return the compatible variable
   *
   * @return compatible
   */
  public boolean getCompatible() {
    return this.compatible;
  }

  /**
   * Return the string representation
   *
   * @return String representation
   */
  @Override
  public String toString() {
    return this.getClass().getName()
        + "{"
        + "compatible="
        + this.compatible
        + "}";
  }
}
