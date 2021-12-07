package org.carpooling.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CountCreditsUseCase {

  private final TripRepository tripRepository;
  private final Map<Carpooler, Float> credits;

  public CountCreditsUseCase(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
    credits = new HashMap<>();
  }

  public Map<Carpooler, Float> execute() {
    List<Trip> trips = tripRepository.findAll();
    trips.forEach(this::updateCreditsForTrip);
    return credits;
  }

  private void updateCreditsForTrip(Trip trip) {
    Carpooler driver = trip.driver();
    Set<Carpooler> passengers = trip.passengers();

    if (passengers.isEmpty())
      return;

    updateDriverCredits(trip, driver);
    passengers.forEach(passenger -> updatePassengerCredits(trip, passenger));
  }

  private void updateDriverCredits(Trip trip, Carpooler driver) {
    updateCreditsForCarpooler(driver, calculateDriverCredits(trip, driver));
  }

  private void updatePassengerCredits(Trip trip, Carpooler passenger) {
    updateCreditsForCarpooler(passenger, calculatePassengerCredits(trip, passenger));
  }

  private void updateCreditsForCarpooler(Carpooler driver, float value) {
    credits.put(driver, value);
  }

  private float calculateDriverCredits(Trip trip, Carpooler driver) {
    return calculateCarpoolerCredits(trip, driver) + 1;
  }

  private float calculatePassengerCredits(Trip trip, Carpooler passenger) {
    return calculateCarpoolerCredits(trip, passenger);
  }

  private float calculateCarpoolerCredits(Trip trip, Carpooler carpooler) {
    Float currentCarpoolerCredits = credits.getOrDefault(carpooler, 0f);
    return calculateCarpoolerCredits(currentCarpoolerCredits, trip.numberOfCarpoolers());
  }

  private float calculateCarpoolerCredits(Float currentCarpoolerCredits, int numberOfCarpoolers) {
    return currentCarpoolerCredits - 1 / (float) numberOfCarpoolers;
  }

}
