package org.carpooling;

import java.util.List;

public interface TripRepository {

  void add(Trip trip);
  List<Trip> findAll();

}
