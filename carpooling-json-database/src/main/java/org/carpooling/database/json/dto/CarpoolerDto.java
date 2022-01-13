package org.carpooling.database.json.dto;

import java.util.Objects;

public final class CarpoolerDto {

  public final String id;
  public final String displayName;

  public CarpoolerDto(String id) {
    this.id = id;
    displayName = null;
  }

  public CarpoolerDto(String id, String displayName) {
    this.id = id;
    this.displayName = displayName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CarpoolerDto that = (CarpoolerDto) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
