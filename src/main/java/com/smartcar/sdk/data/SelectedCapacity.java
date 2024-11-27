package com.smartcar.sdk.data;

/** Data class for an individual available capacity. */
public class SelectedCapacity {
    private double nominal;
    private String source;

    /** No-argument constructor for deserialization. */
    public SelectedCapacity() {
    }

    /** Gets the capacity. */
    public double getNominal() {
        return this.nominal;
    }

    /** Gets the source of the capacity. */
    public String getSource() {
        return this.source;
    }
  
  /** @return a stringified representation of SelectedCapacity*/
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "nominal=" + nominal + ", source=" + source + '}';
  }
}
