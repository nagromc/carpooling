package org.carpooling;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InMemoryTripRepository implements TripRepository {

  private final List<Trip> trips;

  public InMemoryTripRepository() {
    this.trips = new ArrayList<>();
  }

  @Override
  public void add(Carpooler driver, Set<Carpooler> passengers) {
    trips.add(new Trip(driver, passengers));
  }

  @Override
  public List<Trip> findAll() {
    return trips;
  }

}
