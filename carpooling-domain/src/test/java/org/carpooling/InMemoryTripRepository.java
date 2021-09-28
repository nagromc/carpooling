package org.carpooling;

import java.util.*;

public class InMemoryTripRepository implements TripRepository {

  private final Map<Carpooler, List<Set<Carpooler>>> trips;

  public InMemoryTripRepository() {
    this.trips = new HashMap<>();
  }

  @Override
  public void add(Carpooler driver, Set<Carpooler> passengers) {
    List<Set<Carpooler>> tripsDoneByDriver = trips.get(driver);
    if (tripsDoneByDriver == null) tripsDoneByDriver = new ArrayList<>();

    tripsDoneByDriver.add(passengers);
    trips.put(driver, tripsDoneByDriver);
  }

  @Override
  public List<Set<Carpooler>> findTripsByDriver(Carpooler driver) {
    List<Set<Carpooler>> trips = this.trips.get(driver);
    return trips == null ? new ArrayList<>() : trips;
  }

}
