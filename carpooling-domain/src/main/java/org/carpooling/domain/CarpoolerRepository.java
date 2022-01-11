package org.carpooling.domain;

import java.util.Set;

public interface CarpoolerRepository {

  void add(Carpooler carpooler);
  Carpooler findById(String id);
  Set<Carpooler> findAll();

}
