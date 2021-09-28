package org.carpooling;

import java.util.List;
import java.util.Set;

public interface TripRepository {

  void add(String driver, Set<String> passengers);
  List<Set<String>> findTripsByDriver(String driver);

}
