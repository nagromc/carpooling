package org.carpooling.domain;

import java.time.LocalDate;
import java.util.Set;

public class CarPoolUseCase {

  private final TripRepository tripRepository;

  public CarPoolUseCase(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public void execute(LocalDate date, Set<Carpooler> drivers, Set<Carpooler> passengers) {
    if (passengers == null || passengers.isEmpty())
      return;

    tripRepository.add(new Trip(date, drivers, passengers));
  }

}
