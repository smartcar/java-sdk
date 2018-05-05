package com.smartcar.sdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The base object representing parsed API response data.
 */
class ApiData {
  private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  private String data;

  /**
   * Initializes a new instance with the specified data.
   *
   * @param data the data to be included
   */
  public ApiData(String data) {
    this.data = data;
  }

  /**
   * Returns the stored data string.
   *
   * @return the stored data
   */
  public String toString() {
    return this.data;
  }
}
