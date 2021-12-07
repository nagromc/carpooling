package org.carpooling;

import java.util.Set;

public class CarPoolUseCase {

  private final TripRepository tripRepository;

  public CarPoolUseCase(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public void execute(Carpooler driver, Set<Carpooler> passengers) {
    if (passengers == null || passengers.isEmpty())
      return;

    tripRepository.add(new Trip(driver, passengers));
  }

}
