package org.carpooling.domain;

public record Carpooler(String name) {

  @Override
  public String toString() {
    return name;
  }

}
