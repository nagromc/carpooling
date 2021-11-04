package org.carpooling;

import java.util.Set;

public record Trip(Carpooler driver, Set<Carpooler> passengers) {

  public int numberOfCarpoolers() {
    return passengers.size() + 1;
  }

}
