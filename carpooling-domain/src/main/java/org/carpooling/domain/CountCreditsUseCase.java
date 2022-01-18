package org.carpooling.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CountCreditsUseCase {

  private final TripRepository tripRepository;
  private final Map<Carpooler, Float> credits;

  public CountCreditsUseCase(TripRepository tripRepository) {
    this(tripRepository, new HashMap<>());
  }

  CountCreditsUseCase(TripRepository tripRepository, Map<Carpooler, Float> credits) {
    this.tripRepository = tripRepository;
    this.credits = credits;
  }

  public Map<Carpooler, Float> execute() {
    List<Trip> trips = tripRepository.findAll();
    trips.forEach(this::updateCreditsForTrip);
    validateCreditsSanityCheck();
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

  private void updateCreditsForCarpooler(Carpooler carpooler, float value) {
    credits.put(carpooler, value);
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

  private void validateCreditsSanityCheck() {
    double sum = credits.values().stream()
      .mapToDouble(Float::doubleValue)
      .sum();

    if (!FloatingPointComparator.equals(sum, 0, 0.00001f))
      throw new InconsistentCalculatedCreditsException(sum);
  }

}
