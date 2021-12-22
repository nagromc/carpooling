package org.carpooling.database.json.dto;

import java.util.Objects;

public final class CarpoolerDto {

  public final String name;

  public CarpoolerDto(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CarpoolerDto that = (CarpoolerDto) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

}
