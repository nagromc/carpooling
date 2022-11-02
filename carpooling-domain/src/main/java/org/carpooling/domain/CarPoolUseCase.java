package org.carpooling.domain;

import java.time.LocalDate;
import java.util.Set;

public class CarPoolUseCase {

  private final TripRepository tripRepository;

  public CarPoolUseCase(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public void execute(LocalDate date, Carpooler driver, Set<Carpooler> passengers) {
    if (passengers == null || passengers.isEmpty())
      return;

    tripRepository.add(new Trip(date, driver, passengers));
  }

}
