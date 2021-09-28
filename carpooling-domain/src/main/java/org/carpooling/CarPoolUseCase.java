package org.carpooling;

import java.util.Set;

public class CarPoolUseCase {

  private final TripRepository tripRepository;

  public CarPoolUseCase(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public void execute(String driver, Set<String> passengers) {
    if (passengers == null || passengers.isEmpty())
      return;

    tripRepository.add(driver, passengers);
  }

}
