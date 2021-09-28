package org.carpooling;

import java.util.Collection;

public class CountOwedTripsUseCase {
  private final TripRepository tripRepository;

  public CountOwedTripsUseCase(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public long execute(Carpooler debtor, Carpooler creditor) {
    return countNumberOfTrips(creditor, debtor) - countNumberOfTrips(debtor, creditor);
  }

  private long countNumberOfTrips(Carpooler driver, Carpooler passenger) {
    return tripRepository.findTripsByDriver(driver).stream()  // [ [A], [A, B], [B] ]
      .flatMap(Collection::stream) // [ A, A, B, B ]
      .filter(passengers -> passengers.equals(passenger)) // [ B, B ]
      .count();
  }

}
