package org.carpooling.rest;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.ListCarpoolersUseCase;
import org.carpooling.rest.adapter.CarpoolerDtoAdapter;
import org.carpooling.rest.dto.CarpoolerDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping(value = "carpoolers")
public class CarpoolersController {

  private final ListCarpoolersUseCase listCarpoolersUseCase;

  public CarpoolersController(ListCarpoolersUseCase listCarpoolersUseCase) {
    this.listCarpoolersUseCase = listCarpoolersUseCase;
  }

  @GetMapping
  public List<CarpoolerDto> all() {
    Set<Carpooler> carpoolers = listCarpoolersUseCase.execute();

    return carpoolers.stream()
      .map(carpooler -> new CarpoolerDtoAdapter(carpooler).convert())
      .toList();
  }

}
