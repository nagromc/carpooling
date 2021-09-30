package org.carpooling;

public class Carpooler {

  private final String name;

  public Carpooler(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

}
