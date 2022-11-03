package org.carpooling.rest;

import org.carpooling.domain.CarPoolUseCase;
import org.carpooling.domain.ListTripsUseCase;
import org.carpooling.rest.adapter.TripAdapter;
import org.carpooling.rest.adapter.TripDtoAdapter;
import org.carpooling.rest.dto.TripDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "trips")
public class TripsController {

  private final ListTripsUseCase listTripsUseCase;
  private final CarPoolUseCase carPoolUseCase;

  public TripsController(ListTripsUseCase listTripsUseCase,
                         CarPoolUseCase carPoolUseCase) {
    this.listTripsUseCase = listTripsUseCase;
    this.carPoolUseCase = carPoolUseCase;
  }

  @GetMapping
  public List<TripDto> all() {
    var trips = listTripsUseCase.execute();

    return trips.stream()
      .map(trip -> new TripDtoAdapter(trip).convert())
      .toList();
  }

  @PostMapping
  public void add(@RequestBody TripDto tripDto) {
    var trip = new TripAdapter(tripDto).convert();

    carPoolUseCase.execute(trip.date(), trip.driver(), trip.passengers());
  }

}
