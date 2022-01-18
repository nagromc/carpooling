package org.carpooling.domain;

import java.util.Locale;

public class InconsistentCalculatedCreditsException extends RuntimeException {

  private final double calculatedSum;

  public InconsistentCalculatedCreditsException(double calculatedSum) {
    super(String.format(Locale.US, "The sum of all credits should be 0, but was [%f]", calculatedSum));
    this.calculatedSum = calculatedSum;
  }

  public double getCalculatedSum() {
    return calculatedSum;
  }

}
