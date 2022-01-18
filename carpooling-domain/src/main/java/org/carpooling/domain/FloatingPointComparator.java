package org.carpooling.domain;

public class FloatingPointComparator {

  public static boolean equals(double left, int right, float precision) {
    return Math.abs(left - right) < precision;
  }

}
