package org.carpooling.config;

import org.carpooling.database.json.FileDatabaseNotFoundException;
import org.carpooling.database.json.JsonFileCarpoolerRepository;
import org.carpooling.database.json.JsonFileTripRepository;
import org.carpooling.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.File;

@Component
public class CarpoolingAppConfiguration {

  private final DatabaseConfiguration databaseConfig;

  public CarpoolingAppConfiguration(DatabaseConfiguration databaseConfig) {
    this.databaseConfig = databaseConfig;
  }

  @Bean
  public TripRepository tripRepository() throws FileDatabaseNotFoundException {
    return new JsonFileTripRepository(new File(databaseConfig.getBasePath() + "/trip.json"));
  }

  @Bean
  public CarpoolerRepository carpoolerRepository() throws FileDatabaseNotFoundException {
    return new JsonFileCarpoolerRepository(new File(databaseConfig.getBasePath() + "/carpooler.json"));
  }

  @Bean
  public ListTripsUseCase listTripsUseCase(TripRepository tripRepository) {
    return new ListTripsUseCase(tripRepository);
  }

  @Bean
  public CarPoolUseCase carPoolUseCase(TripRepository tripRepository) {
    return new CarPoolUseCase(tripRepository);
  }

  @Bean
  @RequestScope
  public CountCreditsUseCase countCreditsUseCase(TripRepository tripRepository) {
    return new CountCreditsUseCase(tripRepository);
  }

  @Bean
  public ListCarpoolersUseCase listCarpoolersUseCase(CarpoolerRepository carpoolerRepository) {
    return new ListCarpoolersUseCase(carpoolerRepository);
  }

}
