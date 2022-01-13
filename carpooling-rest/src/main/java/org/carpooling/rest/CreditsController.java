package org.carpooling.rest;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.CountCreditsUseCase;
import org.carpooling.rest.adapter.CarpoolerDtoAdapter;
import org.carpooling.rest.dto.CarpoolerDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(value = "credits")
public class CreditsController {

  private final CountCreditsUseCase countCreditsUseCase;

  public CreditsController(CountCreditsUseCase countCreditsUseCase) {
    this.countCreditsUseCase = countCreditsUseCase;
  }

  @GetMapping
  public Map<CarpoolerDto, Float> all() {
    Map<Carpooler, Float> credits = countCreditsUseCase.execute();

    return credits.entrySet().stream()
      .collect(
        Collectors.toMap(
          entry -> new CarpoolerDtoAdapter(entry.getKey()).convert(),
          Map.Entry::getValue
        )
      );
  }

}
