package com.smartcar.sdk.data;

/**
 * A container for the compatibility endpoint
 */
public class Compatibility extends ApiData {
	private boolean compatibility;
	
	/**
	 * Initializes a new instance of Compatibility
	 * 
	 * @param compatibility whether the vehicle is compatible with Smartcar
	 */
	public Compatibility(final boolean compatibility) {
		this.compatibility = compatibility;
	}
	
	/**
	 * Return the compatibility
	 * 
	 * @return compatibility
	 */
	public boolean getCompatibility() {
		return compatibility;
	}
	
	/**
	 * Sets the compatibility
	 * 
	 * @param compatibility whether the vehicle is compatible with Smartcar
	 */
	public void setCompatibility(boolean compatibility) {
		this.compatibility = compatibility;
	}
	
	
}
