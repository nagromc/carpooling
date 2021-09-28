package org.carpooling;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

import java.util.Collections;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CarpoolingStepdefs implements En {

  private final InMemoryTripRepository tripRepository;
  private final CarPoolUseCase carPoolUseCase;
  private final CountOwedTripsUseCase countOwedTripsUseCase;

  public CarpoolingStepdefs() {
    tripRepository = new InMemoryTripRepository();
    carPoolUseCase = new CarPoolUseCase(tripRepository);
    countOwedTripsUseCase = new CountOwedTripsUseCase(tripRepository);

    Given("{string} drove {int} time(s) with {string}", (String driver, Integer initialNumberOfTrips, String passenger) -> {
      IntStream.range(0, initialNumberOfTrips)
        .forEach(i -> tripRepository.add(driver, Set.of(passenger)));
    });

    When("{string} drives with {string}", (String driver, String passenger) -> {
      Set<String> passengers;
      if (passenger.equals("nobody"))
        passengers = Collections.emptySet();
      else
        passengers = Set.of(passenger);

      carPoolUseCase.execute(driver, passengers);
    });

    When("{string} drives with:", (String driver, DataTable passengerDataTable) -> {
      Set<String> passengers = Set.copyOf(passengerDataTable.asList());
      carPoolUseCase.execute(driver, passengers);
    });

    Then("{string} should owe {int} trip(s) to {string}", (String debtor, Integer expectedOwedTrips, String creditor) -> {
      long actualOwedTrips = countOwedTripsUseCase.execute(debtor, creditor);
      assertEquals(expectedOwedTrips.longValue(), actualOwedTrips);
    });

  }

}
