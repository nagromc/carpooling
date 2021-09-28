package org.carpooling;

import java.util.*;

public class InMemoryTripRepository implements TripRepository {

  private final Map<String, List<Set<String>>> trips;

  public InMemoryTripRepository() {
    this.trips = new HashMap<>();
  }

  @Override
  public void add(String driver, Set<String> passengers) {
    List<Set<String>> tripsDoneByDriver = trips.get(driver);
    if (tripsDoneByDriver == null) tripsDoneByDriver = new ArrayList<>();

    tripsDoneByDriver.add(passengers);
    trips.put(driver, tripsDoneByDriver);
  }

  @Override
  public List<Set<String>> findTripsByDriver(String driver) {
    List<Set<String>> trips = this.trips.get(driver);
    return trips == null ? new ArrayList<>() : trips;
  }

}
