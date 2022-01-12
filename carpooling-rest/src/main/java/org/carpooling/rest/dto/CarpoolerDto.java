package org.carpooling.rest.dto;

public record CarpoolerDto(String id) {

  @Override
  public String toString() {
    return id;
  }

}
