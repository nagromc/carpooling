package org.carpooling.domain;

public record Carpooler(String id) {

  @Override
  public String toString() {
    return id;
  }

}
