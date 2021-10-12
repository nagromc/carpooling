package org.carpooling;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CarpoolingStepdefs implements En {

  private CarpoolerRepository carpoolerRepository;
  private TripRepository tripRepository;
  private CarPoolUseCase carPoolUseCase;
  private CountOwedTripsUseCase countOwedTripsUseCase;
  private Map<CarpoolerTuple, Long> totalOwedTrips;

  public CarpoolingStepdefs() {

    Before(() -> {
      carpoolerRepository = new InMemoryCarpoolerRepository();
      tripRepository = new InMemoryTripRepository();
      carPoolUseCase = new CarPoolUseCase(tripRepository);
      countOwedTripsUseCase = new CountOwedTripsUseCase(tripRepository, carpoolerRepository);
    });

    Given("the following trips:", (DataTable initialTripsDataTable) -> {
      List<InitialTrip> initialTrips = initialTripsDataTable.asList(InitialTrip.class);
      initialTrips.forEach(
        initialTrip -> IntStream.range(0, initialTrip.nbOfTrips).forEach(
          i -> tripRepository.add(initialTrip.driver, Set.of(initialTrip.passenger))
        )
      );
    });

    When("the owed trips are counted", () ->
      totalOwedTrips = countOwedTripsUseCase.execute()
    );

    When("{string} drives alone", (String driverName) -> {
      Carpooler driver = carpoolerRepository.findByName(driverName);

      carPoolUseCase.execute(driver, Collections.emptySet());
      totalOwedTrips = countOwedTripsUseCase.execute();
    });

    When("{string} drives with:", (String driverName, DataTable passengersNamesDataTable) -> {
      Carpooler driver = carpoolerRepository.findByName(driverName);
      Set<Carpooler> passengers = passengersNamesDataTable.asList().stream()
        .map(carpoolerRepository::findByName)
        .collect(Collectors.toSet());

      carPoolUseCase.execute(driver, passengers);
      totalOwedTrips = countOwedTripsUseCase.execute();
    });

    Then("the owed trips should be:", (DataTable expectedOwedTrips) -> {
      List<Debt> debts = expectedOwedTrips.asList(Debt.class);
      debts.forEach(debt -> {
        Long actualOwedTrips = totalOwedTrips.get(new CarpoolerTuple(debt.debtor(), debt.creditor()));
        assertEquals(debt.numberOfOwedTrips().longValue(), actualOwedTrips.longValue());
      });
    });

    DataTableType((Map<String, String> row) -> new InitialTrip(
      findCarpoolerOrCreate(row.get("driver")),
      Integer.parseInt(row.get("nbOfTrips")),
      findCarpoolerOrCreate(row.get("passenger"))
    ));

    DataTableType((Map<String, String> row) -> new Debt(
      carpoolerRepository.findByName(row.get("debtor")),
      Long.parseLong(row.get("nbOfOwedTrips")),
      carpoolerRepository.findByName(row.get("creditor"))
    ));

  }

  private Carpooler findCarpoolerOrCreate(String carpoolerName) {
    Carpooler carpooler = carpoolerRepository.findByName(carpoolerName);
    if (carpooler == null) {
      carpooler = new Carpooler(carpoolerName);
      carpoolerRepository.add(carpooler);
    }
    return carpooler;
  }


  private record InitialTrip(Carpooler driver, Integer nbOfTrips, Carpooler passenger) {

  }

  private record Debt(Carpooler debtor, Long numberOfOwedTrips, Carpooler creditor) {

  }

}
