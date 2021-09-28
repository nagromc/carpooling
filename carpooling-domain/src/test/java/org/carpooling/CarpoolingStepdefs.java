package org.carpooling;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CarpoolingStepdefs implements En {

  private final CarpoolerRepository carpoolerRepository;
  private final TripRepository tripRepository;
  private final CarPoolUseCase carPoolUseCase;
  private final CountOwedTripsUseCase countOwedTripsUseCase;

  public CarpoolingStepdefs() {
    carpoolerRepository = new InMemoryCarpoolerRepository();
    tripRepository = new InMemoryTripRepository();
    carPoolUseCase = new CarPoolUseCase(tripRepository);
    countOwedTripsUseCase = new CountOwedTripsUseCase(tripRepository);

    Given("{string} drove {int} time(s) with {string}", (String driverName, Integer initialNumberOfTrips, String passengerName) -> {
      Carpooler driver = findCarpoolerOrCreate(driverName);
      Carpooler passenger = findCarpoolerOrCreate(passengerName);

      IntStream.range(0, initialNumberOfTrips)
        .forEach(i -> tripRepository.add(driver, Set.of(passenger)));
    });

    // TODO merge whens
    When("{string} drives with {string}", (String driverName, String passengerName) -> {
      Carpooler driver = carpoolerRepository.findByName(driverName);
      Set<Carpooler> passengers = getPassenger(passengerName);

      carPoolUseCase.execute(driver, passengers);
    });

    // TODO merge whens
    When("{string} drives with:", (String driverName, DataTable passengersNamesDataTable) -> {
      Carpooler driver = carpoolerRepository.findByName(driverName);
      Set<Carpooler> passengers = passengersNamesDataTable.asList().stream()
        .map(carpoolerRepository::findByName)
        .collect(Collectors.toSet());

      carPoolUseCase.execute(driver, passengers);
    });

    Then("{string} should owe {int} trip(s) to {string}", (String debtorName, Integer expectedOwedTrips, String creditorName) -> {
      Carpooler debtor = carpoolerRepository.findByName(debtorName);
      Carpooler creditor = carpoolerRepository.findByName(creditorName);

      long actualOwedTrips = countOwedTripsUseCase.execute(debtor, creditor);
      assertEquals(expectedOwedTrips.longValue(), actualOwedTrips);
    });

  }

  private Set<Carpooler> getPassenger(String passengerName) {
    Carpooler passenger = carpoolerRepository.findByName(passengerName);

    if (passengerName.equals("nobody"))
      return Collections.emptySet();

    return Set.of(passenger);
  }

  private Carpooler findCarpoolerOrCreate(String carpoolerName) {
    Carpooler carpooler = carpoolerRepository.findByName(carpoolerName);
    if (carpooler == null) {
      carpooler = new Carpooler(carpoolerName);
      carpoolerRepository.add(carpooler);
    }
    return carpooler;
  }

}
