package org.carpooling;

import java.util.*;

public class InMemoryTripRepository implements TripRepository {

  private final Map<Carpooler, List<Set<Carpooler>>> trips;

  public InMemoryTripRepository() {
    this.trips = new HashMap<>();
  }

  @Override
  public void add(Carpooler driver, Set<Carpooler> passengers) {
    trips.computeIfAbsent(driver, carpooler -> new ArrayList<>())
      .add(passengers);
  }

  @Override
  public List<Set<Carpooler>> findTripsByDriver(Carpooler driver) {
    return trips.getOrDefault(driver, new ArrayList<>());
  }

}
