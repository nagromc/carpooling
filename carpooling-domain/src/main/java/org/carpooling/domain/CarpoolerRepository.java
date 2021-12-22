package org.carpooling.domain;

import java.util.Set;

public interface CarpoolerRepository {

  void add(Carpooler carpooler);
  Carpooler findByName(String name);
  Set<Carpooler> findAll();

}
