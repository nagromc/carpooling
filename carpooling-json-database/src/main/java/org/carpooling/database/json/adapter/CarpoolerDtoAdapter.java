package org.carpooling.database.json.adapter;

import org.carpooling.database.json.dto.CarpoolerDto;
import org.carpooling.domain.Carpooler;

public class CarpoolerDtoAdapter {

  private final Carpooler carpooler;

  public CarpoolerDtoAdapter(Carpooler carpooler) {
    validateArguments(carpooler);

    this.carpooler = carpooler;
  }

  private void validateArguments(Carpooler carpooler) {
    if (carpooler == null) throw new IllegalArgumentException();
  }

  public CarpoolerDto convert() {
    return new CarpoolerDto(carpooler.name());
  }

}
