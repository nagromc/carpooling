package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.rest.dto.CarpoolerDto;

public class CarpoolerDtoAdapter {

  private final Carpooler carpooler;

  public CarpoolerDtoAdapter(Carpooler carpooler) {
    this.carpooler = carpooler;

    validate();
  }

  private void validate() {
    if (carpooler == null)
      throw new IllegalArgumentException("Carpooler cannot be null");
  }

  public CarpoolerDto convert() {
    return new CarpoolerDto(carpooler.id());
  }
}
