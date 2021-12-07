package org.carpooling;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTripRepository implements TripRepository {

  private final List<Trip> trips;

  public InMemoryTripRepository() {
    this.trips = new ArrayList<>();
  }

  @Override
  public void add(Trip trip) {
    trips.add(trip);
  }

  @Override
  public List<Trip> findAll() {
    return trips;
  }

}
