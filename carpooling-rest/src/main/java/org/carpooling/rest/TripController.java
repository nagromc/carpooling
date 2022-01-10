package org.carpooling.rest;

import org.carpooling.domain.CarPoolUseCase;
import org.carpooling.domain.ListTripsUseCase;
import org.carpooling.domain.Trip;
import org.carpooling.rest.adapter.TripAdapter;
import org.carpooling.rest.adapter.TripDtoAdapter;
import org.carpooling.rest.dto.TripDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "trip")
public class TripController {

  private final ListTripsUseCase listTripsUseCase;
  private final CarPoolUseCase carPoolUseCase;

  public TripController(ListTripsUseCase listTripsUseCase,
                        CarPoolUseCase carPoolUseCase) {
    this.listTripsUseCase = listTripsUseCase;
    this.carPoolUseCase = carPoolUseCase;
  }

  @GetMapping(value = "all")
  public List<TripDto> all() {
    List<Trip> trips = listTripsUseCase.execute();

    return trips.stream()
      .map(trip -> new TripDtoAdapter(trip).convert())
      .toList();
  }

  @PostMapping
  public void add(@RequestBody TripDto tripDto) {
    Trip trip = new TripAdapter(tripDto).convert();

    carPoolUseCase.execute(trip.driver(), trip.passengers());
  }

}
