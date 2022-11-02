package org.carpooling.domain;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

import java.time.LocalDate;
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
  private CountCreditsUseCase countCreditsUseCase;
  private Map<Carpooler, Float> carpoolersCredits;

  public CarpoolingStepdefs() {

    Before(() -> {
      carpoolerRepository = new InMemoryCarpoolerRepository();
      tripRepository = new InMemoryTripRepository();
      carPoolUseCase = new CarPoolUseCase(tripRepository);
      countCreditsUseCase = new CountCreditsUseCase(tripRepository);
    });

    Given("the following trips:", (DataTable initialTripsDataTable) -> {
      List<InitialTrip> initialTrips = initialTripsDataTable.asList(InitialTrip.class);
      initialTrips.forEach(
        initialTrip -> IntStream.range(0, initialTrip.nbOfTrips).forEach(
          i -> tripRepository.add(new Trip(initialTrip.date, initialTrip.driver, Set.of(initialTrip.passenger)))
        )
      );
    });

    When("the credits are counted", () ->
      carpoolersCredits = countCreditsUseCase.execute()
    );

    When("{string} drives on {string} alone", (String driverId, String dateString) -> {
      Carpooler driver = carpoolerRepository.findById(driverId);
      LocalDate date = LocalDate.parse(dateString);

      carPoolUseCase.execute(date, driver, Collections.emptySet());
    });

    When("{string} drives on {string} with:", (String driverId, String dateString, DataTable passengersIdsDataTable) -> {
      Carpooler driver = carpoolerRepository.findById(driverId);
      LocalDate date = LocalDate.parse(dateString);
      Set<Carpooler> passengers = passengersIdsDataTable.asList().stream()
        .map(carpoolerRepository::findById)
        .collect(Collectors.toSet());

      carPoolUseCase.execute(date, driver, passengers);
    });

    Then("the credits should be:", (DataTable expectedCreditsDataTable) -> {
      List<Credit> expectedCredits = expectedCreditsDataTable.asList(Credit.class);
      expectedCredits.forEach(expectedCredit -> {
        Float actualCredit = carpoolersCredits.get(expectedCredit.carpooler());
        assertEquals(expectedCredit.value(), actualCredit, 0.001);
      });
    });

    DataTableType((Map<String, String> row) -> new InitialTrip(
      LocalDate.parse(row.get("date")),
      findCarpoolerOrCreate(row.get("driver")),
      Integer.parseInt(row.get("nbOfTrips")),
      findCarpoolerOrCreate(row.get("passenger"))
    ));

    DataTableType((Map<String, String> row) -> new Credit(
      carpoolerRepository.findById(row.get("carpooler")),
      Float.parseFloat(row.get("credit"))
    ));

  }

  private Carpooler findCarpoolerOrCreate(String carpoolerId) {
    Carpooler carpooler = carpoolerRepository.findById(carpoolerId);
    if (carpooler == null) {
      carpooler = new Carpooler(carpoolerId);
      carpoolerRepository.add(carpooler);
    }
    return carpooler;
  }


  private record InitialTrip(LocalDate date, Carpooler driver, Integer nbOfTrips, Carpooler passenger) {

  }

  private record Credit(Carpooler carpooler, Float value) {

  }

}
