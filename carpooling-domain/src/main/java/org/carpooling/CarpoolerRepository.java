package org.carpooling;

public interface CarpoolerRepository {

  Carpooler findByName(String name);
  void add(Carpooler carpooler);

}
