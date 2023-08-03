package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.carpooling.rest.dto.TripDto;

import java.util.stream.Collectors;

public class TripAdapter {

  private final TripDto tripDto;

  public TripAdapter(TripDto tripDto) {
    this.tripDto = tripDto;
    validate();
  }

  private void validate() {
    if (tripDto == null)
      throw new IllegalArgumentException("TripDto cannot be null");
  }

  public Trip convert() {
    var date = tripDto.date();
    var drivers = tripDto.drivers().stream()
      .map(Carpooler::new)
      .collect(Collectors.toSet());
    var passengers = tripDto.passengers().stream()
      .map(Carpooler::new)
      .collect(Collectors.toSet());

    return new Trip(date, drivers, passengers);
  }

}
