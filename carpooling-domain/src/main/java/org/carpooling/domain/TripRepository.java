package org.carpooling.domain;

import java.util.List;

public interface TripRepository {

  void add(Trip trip);
  List<Trip> findAll();

}
