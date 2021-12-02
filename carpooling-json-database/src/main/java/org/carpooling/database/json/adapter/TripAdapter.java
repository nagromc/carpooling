package org.carpooling.database.json.adapter;

import org.carpooling.Carpooler;
import org.carpooling.Trip;
import org.carpooling.database.json.dto.CarpoolerDto;
import org.carpooling.database.json.dto.TripDto;

import java.util.Set;
import java.util.stream.Collectors;

public class TripAdapter {

  private final TripDto tripDto;

  public TripAdapter(TripDto tripDto) {
    validateArguments(tripDto);

    this.tripDto = tripDto;
  }

  private void validateArguments(TripDto tripDto) {
    if (tripDto == null) throw new IllegalArgumentException();
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
