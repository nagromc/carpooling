package org.carpooling.domain;

public record Carpooler(String id, String displayName) {

  public Carpooler(String id) {
    this(id, "Anonymous");
  }

  @Override
  public String toString() {
    return id;
  }

}
