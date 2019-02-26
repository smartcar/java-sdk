package com.smartcar.sdk.data;

/**
 * A container for the compatibility endpoint
 */
public class Compatibility extends ApiData {
	private boolean compatible;

	/**
	 * Initializes a new instance of Compatibility
	 *
	 * @param compatible whether the vehicle is compatible with Smartcar
	 */
	public Compatibility(final boolean compatible) {
		this.compatible = compatible;
	}

	/**
	 * Return the compatible variable
	 *
	 * @return compatible
	 */
	public boolean getCompatible() {
		return this.compatible;
	}

	/**
	 * Sets the compatible variable
	 *
	 * @param compatible whether the vehicle is compatible with Smartcar
	 */
	public void setCompatible(boolean compatible) {
		this.compatible = compatible;
	}

  /**
   * Return the string representation
   *
   * @return String representation
   */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
      "compatible=" + Boolean.toString(this.compatible) +
      "}";
  }

}
