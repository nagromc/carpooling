package org.carpooling.domain;

import java.util.Set;

public class ListCarpoolersUseCase {

  private final CarpoolerRepository carpoolerRepository;

  public ListCarpoolersUseCase(CarpoolerRepository carpoolerRepository) {
    this.carpoolerRepository = carpoolerRepository;
  }

  public Set<Carpooler> execute() {
    return carpoolerRepository.findAll();
  }
}
