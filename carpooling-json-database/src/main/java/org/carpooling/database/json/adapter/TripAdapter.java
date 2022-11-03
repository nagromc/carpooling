package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.CarpoolerDto;
import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Trip;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class TripAdapter {

  private final TripDto tripDto;

  public TripAdapter(TripDto tripDto) {
    validateArguments(tripDto);

    this.tripDto = tripDto;
  }

  private void validateArguments(TripDto tripDto) {
    if (tripDto == null) throw new FileDatabaseException("Domain object cannot be null");
  }

  public Trip convert() {
    var driverDto = new CarpoolerDto(tripDto.driverId);

    var date = LocalDate.parse(tripDto.date);
    var driver = new CarpoolerAdapter(driverDto).convert();

    var passengers = tripDto.passengersId.stream()
      .map(id -> {
        var passengerDto = new CarpoolerDto(id);
        return new CarpoolerAdapter(passengerDto).convert();
      })
      .collect(Collectors.toSet());

    return new Trip(date, driver, passengers);
  }

}
