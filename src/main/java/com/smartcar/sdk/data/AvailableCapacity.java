package com.smartcar.sdk.data;

/** Data class for an individual available capacity. */
public class AvailableCapacity {
    private double capacity;
    private String description;

    /** No-argument constructor for deserialization. */
    public AvailableCapacity() {
    }

    /** Gets the capacity. */
    public double getCapacity() {
        return this.capacity;
    }

    /** Gets the description of the capacity. */
    public String getDescription() {
        return this.description;
    }
  
  /** @return a stringified representation of AvailableCapacity*/
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "capacity=" + capacity + ", description=" + description + '}';
  }
}
