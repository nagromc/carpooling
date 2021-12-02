package org.carpooling;

public record Carpooler(String name) {

  @Override
  public String toString() {
    return name;
  }

}
