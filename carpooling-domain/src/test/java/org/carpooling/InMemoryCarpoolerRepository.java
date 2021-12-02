package org.carpooling;

import java.util.HashSet;
import java.util.Set;

public class InMemoryCarpoolerRepository implements CarpoolerRepository {

  private final Set<Carpooler> carpoolers;

  public InMemoryCarpoolerRepository() {
    carpoolers = new HashSet<>();
  }

  @Override
  public void add(Carpooler carpooler) {
    carpoolers.add(carpooler);
  }

  @Override
  public Carpooler findByName(String name) {
    return carpoolers.stream() // {A, B, C}
      .filter(c -> c.name().equals(name)) // name = "A" -> {A}
      .findFirst() // A
      .orElse(null); // null
  }

  @Override
  public Set<Carpooler> findAll() {
    return carpoolers;
  }

}
