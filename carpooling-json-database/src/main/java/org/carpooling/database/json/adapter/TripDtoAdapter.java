package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Trip;

import java.util.Set;
import java.util.stream.Collectors;

public class TripDtoAdapter {

  private final Trip trip;

  public TripDtoAdapter(Trip trip) {
    validateArguments(trip);

    this.trip = trip;
  }

  private void validateArguments(Trip trip) {
    if (trip == null) throw new FileDatabaseException("Domain object cannot be null");
  }

  public TripDto convert() {
    String driverName = new CarpoolerDtoAdapter(trip.driver()).convert().name();

    Set<String> passengersName = trip.passengers().stream()
      .map(carpooler -> new CarpoolerDtoAdapter(carpooler).convert().name())
      .collect(Collectors.toSet());

    return new TripDto(
      driverName,
      passengersName
    );
  }

}
