package org.carpooling.domain;

import java.time.LocalDate;
import java.util.Set;

public record Trip(LocalDate date, Carpooler driver, Set<Carpooler> passengers) {

  public int numberOfCarpoolers() {
    return passengers.size() + 1;
  }

}
