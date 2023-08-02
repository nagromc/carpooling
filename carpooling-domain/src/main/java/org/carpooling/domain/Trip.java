package org.carpooling.domain;

import java.time.LocalDate;
import java.util.Set;

public record Trip(LocalDate date, Set<Carpooler> drivers, Set<Carpooler> passengers) {

  public int numberOfCarpoolers() {
    return passengers.size() + numberOfDrivers();
  }

  public int numberOfDrivers() {
    return drivers.size();
  }

}
