package org.carpooling.rest;

import org.carpooling.domain.ListTripsUseCase;
import org.carpooling.domain.Trip;
import org.carpooling.rest.adapter.TripDtoAdapter;
import org.carpooling.rest.dto.TripDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "trip")
public class TripController {

  private final ListTripsUseCase listTripsUseCase;

  public TripController(ListTripsUseCase listTripsUseCase) {
    this.listTripsUseCase = listTripsUseCase;
  }

  @GetMapping(value = "all")
  public List<TripDto> all() {
    List<Trip> trips = listTripsUseCase.execute();

    return trips.stream()
      .map(trip -> new TripDtoAdapter(trip).convert())
      .toList();
  }

}
