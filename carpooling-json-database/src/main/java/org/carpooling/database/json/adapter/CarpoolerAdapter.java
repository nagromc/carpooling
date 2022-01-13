package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.CarpoolerDto;
import org.carpooling.domain.Carpooler;

public class CarpoolerAdapter {

  private final CarpoolerDto carpoolerDto;

  public CarpoolerAdapter(CarpoolerDto carpoolerDto) {
    validateArguments(carpoolerDto);

    this.carpoolerDto = carpoolerDto;
  }

  private void validateArguments(CarpoolerDto carpoolerDto) {
    if (carpoolerDto == null) throw new FileDatabaseException("DTO cannot be null");
  }

  public Carpooler convert() {
    return new Carpooler(carpoolerDto.id, carpoolerDto.displayName);
  }

}
