package org.carpooling.domain;

import java.util.StringJoiner;

public record Carpooler(String id, String displayName) {

  public Carpooler(String id) {
    this(id, "Anonymous");
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Carpooler.class.getSimpleName() + "[", "]")
      .add("id='" + id + "'")
      .add("displayName='" + displayName + "'")
      .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Carpooler carpooler = (Carpooler) o;
    return id.equals(carpooler.id) && displayName.equals(carpooler.displayName);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + displayName.hashCode();
    return result;
  }
}
