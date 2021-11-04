package org.carpooling;

import java.util.List;
import java.util.Set;

public interface TripRepository {

  void add(Carpooler driver, Set<Carpooler> passengers);
  List<Trip> findAll();

}
