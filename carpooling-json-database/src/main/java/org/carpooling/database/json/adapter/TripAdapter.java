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
    var date = LocalDate.parse(tripDto.date);

    var drivers = tripDto.driversId.stream()
      .map(id -> new CarpoolerAdapter(new CarpoolerDto(id)).convert())
      .collect(Collectors.toSet());

    var passengers = tripDto.passengersId.stream()
      .map(id -> new CarpoolerAdapter(new CarpoolerDto(id)).convert())
      .collect(Collectors.toSet());

    return new Trip(date, drivers, passengers);
  }

}
