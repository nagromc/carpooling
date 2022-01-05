package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.carpooling.rest.dto.TripDto;

import java.util.Set;
import java.util.stream.Collectors;

public class TripDtoAdapter {

  private final Trip trip;

  public TripDtoAdapter(Trip trip) {
    this.trip = trip;
    validate();
  }

  private void validate() {
    if (trip == null)
      throw new IllegalArgumentException("Trip cannot be null");
  }

  public TripDto convert() {
    Set<String> passengersName = trip.passengers().stream()
      .map(Carpooler::name)
      .collect(Collectors.toSet());

    return new TripDto(trip.driver().name(), passengersName);
  }

}
