package org.carpooling.domain;

import java.util.Set;

public interface CarpoolerRepository {

  void add(Carpooler carpooler);
  Carpooler findById(String id) throws NotFoundException;
  Set<Carpooler> findAll();


  class NotFoundException extends RuntimeException {
    public NotFoundException(String id) {
      super("Carpooler with id " + id + " not found");
    }
  }
}
