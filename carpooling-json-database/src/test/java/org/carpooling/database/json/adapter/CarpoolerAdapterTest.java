package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.CarpoolerDto;
import org.carpooling.domain.Carpooler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarpoolerAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    FileDatabaseException exception = assertThrows(FileDatabaseException.class, () -> new CarpoolerAdapter(null));
    assertEquals("DTO cannot be null", exception.getMessage());
  }

  @Test
  void givenCarpoolerDto_shouldReturnCarpooler() {
    CarpoolerDto dto = new CarpoolerDto("Alice");
    CarpoolerAdapter adapter = new CarpoolerAdapter(dto);

    Carpooler result = adapter.convert();

    assertEquals("Alice", result.name());
  }

}
