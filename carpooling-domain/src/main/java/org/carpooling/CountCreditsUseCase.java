package org.carpooling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CountCreditsUseCase {

  private final TripRepository tripRepository;

  public CountCreditsUseCase(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public Map<Carpooler, Float> execute() {
    Map<Carpooler, Float> result = new HashMap<>();

    List<Trip> trips = tripRepository.findAll();

    trips.forEach(
      trip -> {
        Carpooler driver = trip.driver();
        Set<Carpooler> passengers = trip.passengers();

        if (passengers.isEmpty())
          return;

        Float currentDriverCredit = result.getOrDefault(driver, 0f);
        result.put(driver, calculateDriverCredit(currentDriverCredit, trip.numberOfCarpoolers()));

        passengers.forEach(
          passenger -> {
            Float currentPassengerCredit = result.getOrDefault(passenger, 0f);
            result.put(passenger, calculatePassengerCredit(currentPassengerCredit, trip.numberOfCarpoolers()));
          }
        );
      }
    );

    return result;
  }

  private float calculateDriverCredit(Float currentDriverCredit, int numberOfCarpoolers) {
    return calculatePassengerCredit(currentDriverCredit, numberOfCarpoolers) + 1;
  }

  private float calculatePassengerCredit(Float currentPassengerCredit, int numberOfCarpoolers) {
    return currentPassengerCredit - 1 / (float) numberOfCarpoolers;
  }

}
