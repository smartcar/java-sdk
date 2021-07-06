package com.smartcar.sdk;

import org.apache.commons.text.CaseUtils;

/** General package utilities. */
public class Utils {
  /**
   * Joins the elements of a string array together, delimited by a separator.
   *
   * @param array the array containing values to be joined
   * @param separator the delimiter to insert between each value
   * @return the joined result
   */
  static String join(String[] array, String separator) {
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        stringBuilder.append(separator);
      }

      stringBuilder.append(array[i]);
    }

    return stringBuilder.toString();
  }

  public static String toCamelCase(String fieldName) {
    if (fieldName.contains("_")) { // checks for snake case
      return CaseUtils.toCamelCase(fieldName, false, '_');
    }
    return fieldName;
  }
}
