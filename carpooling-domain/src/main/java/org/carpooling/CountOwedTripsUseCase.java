package org.carpooling;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountOwedTripsUseCase {

  private final TripRepository tripRepository;
  private final CarpoolerRepository carpoolerRepository;

  public CountOwedTripsUseCase(TripRepository tripRepository, CarpoolerRepository carpoolerRepository) {
    this.tripRepository = tripRepository;
    this.carpoolerRepository = carpoolerRepository;
  }

  public Map<CarpoolerTuple, Long> execute() {
    Set<Carpooler> carpoolers = carpoolerRepository.findAll();

    Map<CarpoolerTuple, Long> result = new HashMap<>();

    // TODO convert to stream
    for (Carpooler debtor: carpoolers) {
      for (Carpooler creditor: carpoolers) {
        if (debtor.equals(creditor))
          continue;

        long owedTrips = calculateOwedTripsBetweenTwoCarpoolers(debtor, creditor);
        result.put(new CarpoolerTuple(debtor, creditor), owedTrips);
      }
    }

    return result;
  }

  private long calculateOwedTripsBetweenTwoCarpoolers(Carpooler debtor, Carpooler creditor) {
    return countNumberOfTrips(creditor, debtor) - countNumberOfTrips(debtor, creditor);
  }

  private long countNumberOfTrips(Carpooler driver, Carpooler passenger) {
    return tripRepository.findTripsByDriver(driver).stream()  // [ [A], [A, B], [B] ]
      .flatMap(Collection::stream) // [ A, A, B, B ]
      .filter(passengers -> passengers.equals(passenger)) // [ B, B ]
      .count();
  }

}
