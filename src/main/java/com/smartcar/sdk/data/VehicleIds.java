package com.smartcar.sdk.data;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;

/** POJO for Smartcar /vehicles endpoint. */
public class VehicleIds extends ApiData {
  @SerializedName("vehicles")
  private String[] vehicleIds;

  private ResponsePaging paging;

  /**
   * Initializes a new instance of VehicleIds
   *
   * @param vehicleIds the vehicleIds to be stored
   */
  public VehicleIds(final String[] vehicleIds, final ResponsePaging paging) {
    this.vehicleIds = vehicleIds;
    this.paging = paging;
  }

  /**
   * Returns the vehicle IDs.
   *
   * @return the vehicle IDs
   */
  public String[] getVehicleIds() {
    return vehicleIds;
  }

  public ResponsePaging getPaging() {
    return this.paging;
  }

  /** @return a stringified representation of VehicleIds */
  @Override
  public String toString() {
    return this.getClass().getName() + "{" + "vehicleIds=" + Arrays.toString(vehicleIds) + '}';
  }
}
