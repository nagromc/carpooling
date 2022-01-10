package org.carpooling.config;

import org.carpooling.database.json.FileDatabaseNotFoundException;
import org.carpooling.database.json.JsonFileTripRepository;
import org.carpooling.domain.CarPoolUseCase;
import org.carpooling.domain.ListTripsUseCase;
import org.carpooling.domain.TripRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CarpoolingAppConfiguration {

  @Bean
  public TripRepository tripRepository() throws FileDatabaseNotFoundException {
    return new JsonFileTripRepository(new File("db/trip.json"));
  }

  @Bean
  public ListTripsUseCase listTripsUseCase(TripRepository tripRepository) {
    return new ListTripsUseCase(tripRepository);
  }

  @Bean
  public CarPoolUseCase carPoolUseCase(TripRepository tripRepository) {
    return new CarPoolUseCase(tripRepository);
  }

}
