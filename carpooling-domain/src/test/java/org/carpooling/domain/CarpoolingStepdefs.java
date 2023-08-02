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
          i -> tripRepository.add(new Trip(initialTrip.date, Set.of(initialTrip.driver), Set.of(initialTrip.passenger)))
        )
      );
    });

    When("the credits are counted", () ->
      carpoolersCredits = countCreditsUseCase.execute()
    );

    When("{string} drives on {string} alone", (String driverId, String dateString) -> {
      var driver = carpoolerRepository.findById(driverId);
      var date = LocalDate.parse(dateString);

      carPoolUseCase.execute(date, Set.of(driver), Collections.emptySet());
    });

    When("{string} drives on {string} with:", (String driverId, String dateString, DataTable passengersIdsDataTable) -> {
      var driver = carpoolerRepository.findById(driverId);
      var date = LocalDate.parse(dateString);
      var passengers = passengersIdsDataTable.asList().stream()
        .map(carpoolerRepository::findById)
        .collect(Collectors.toSet());

      carPoolUseCase.execute(date, Set.of(driver), passengers);
    });

    When("{string} and {string} drive on {string} alone", (String driverId1, String driverId2, String dateString) -> {
      var driver1 = carpoolerRepository.findById(driverId1);
      var driver2 = carpoolerRepository.findById(driverId2);
      var date = LocalDate.parse(dateString);
      var drivers = Set.of(driver1, driver2);

      carPoolUseCase.execute(date, drivers, Collections.emptySet());
    });

    When("{string} and {string} drive on {string} with:",
      (String driverId1, String driverId2, String dateString, DataTable passengersIdsDataTable) -> {
        var driver1 = carpoolerRepository.findById(driverId1);
        var driver2 = carpoolerRepository.findById(driverId2);
        var date = LocalDate.parse(dateString);
        var drivers = Set.of(driver1, driver2);
        var passengers = passengersIdsDataTable.asList().stream()
          .map(carpoolerRepository::findById)
          .collect(Collectors.toSet());

        carPoolUseCase.execute(date, drivers, passengers);
    });

    Then("the credits should be:", (DataTable expectedCreditsDataTable) -> {
      List<Credit> expectedCredits = expectedCreditsDataTable.asList(Credit.class);
      expectedCredits.forEach(expectedCredit -> {
        var actualCredit = carpoolersCredits.get(expectedCredit.carpooler());
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
    var carpooler = carpoolerRepository.findById(carpoolerId);
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
