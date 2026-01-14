package com.smartcar.sdk.data.v3;

public class VehicleAttributes extends JsonApiData {
  private String id;
  private String make;
  private String model;
  private Integer year;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMake() {
    return make;
  }

  public String getModel() {
    return model;
  }

  public Integer getYear() {
    return year;
  }

  @Override
  public String toString() {
    return this.getClass().getName()
        + "{"
        + "id="
        + id
        + ", make='"
        + make
        + '\''
        + ", model='"
        + model
        + '\''
        + ", year="
        + year
        + '}';
  }
}
