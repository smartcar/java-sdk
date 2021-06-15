package com.smartcar.sdk.data;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.lang.reflect.Type;

import okhttp3.Response;

/** The base object representing parsed API response data. */
public class ApiData<T> implements Serializable {
  private static Gson gson =
      new GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();

  private T data;
  private Meta meta;

  /** Default constructor. */
  public ApiData() {}

  /**
   * Initializes a new instance with the specified data.
   *
   * @param data the data to be included
   */
  public ApiData(T data) {
    this.data = data;
  }

  public Meta getMeta() { return this.meta; }

  public void setMeta(Meta meta) { this.meta = meta; }

  /**
   * Returns the stored data string.
   *
   * @return the stored data
   */
  public String toString() {
    return String.valueOf(this.data);
  }
}
