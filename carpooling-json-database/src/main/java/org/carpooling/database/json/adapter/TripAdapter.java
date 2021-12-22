package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.CarpoolerDto;
import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;

import java.util.Set;
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
    CarpoolerDto driverDto = new CarpoolerDto(tripDto.driverName);
    Carpooler driver = new CarpoolerAdapter(driverDto).convert();

    Set<Carpooler> passengers = tripDto.passengersName.stream()
      .map(name -> {
        CarpoolerDto passengerDto = new CarpoolerDto(name);
        return new CarpoolerAdapter(passengerDto).convert();
      })
      .collect(Collectors.toSet());

    return new Trip(driver, passengers);
  }

}