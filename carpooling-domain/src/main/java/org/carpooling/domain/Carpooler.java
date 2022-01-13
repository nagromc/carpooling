package org.carpooling.domain;

public record Carpooler(String id, String displayName) {

  public Carpooler(String id) {
    this(id, "Anonymous");
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Carpooler carpooler = (Carpooler) o;

    return id.equals(carpooler.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

}
