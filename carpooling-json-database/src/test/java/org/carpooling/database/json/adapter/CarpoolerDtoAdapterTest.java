package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.CarpoolerDto;
import org.carpooling.domain.Carpooler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarpoolerDtoAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    FileDatabaseException exception = assertThrows(FileDatabaseException.class, () -> new CarpoolerDtoAdapter(null));
    assertEquals("DTO cannot be null", exception.getMessage());
  }

  @Test
  void givenCarpooler_shouldConvert() {
    Carpooler carpooler = new Carpooler("Alice");
    CarpoolerDtoAdapter adapter = new CarpoolerDtoAdapter(carpooler);

    CarpoolerDto result = adapter.convert();

    assertEquals("Alice", result.name);
  }

}
