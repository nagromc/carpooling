package org.carpooling.rest.dto;

public record CarpoolerDto(String id, String name) {

  @Override
  public String toString() {
    return id;
  }

}
