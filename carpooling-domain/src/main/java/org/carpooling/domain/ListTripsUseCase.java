package org.carpooling.domain;

import java.util.List;

public class ListTripsUseCase {

  private final TripRepository tripRepository;

  public ListTripsUseCase(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public List<Trip> execute() {
    return tripRepository.findAll();
  }

}
