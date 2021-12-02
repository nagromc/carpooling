package org.carpooling.database.json.adapter;

import org.carpooling.Carpooler;
import org.carpooling.database.json.dto.CarpoolerDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarpoolerDtoAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> new CarpoolerDtoAdapter(null));
  }

  @Test
  void givenCarpooler_shouldConvert() {
    Carpooler carpooler = new Carpooler("Alice");
    CarpoolerDtoAdapter adapter = new CarpoolerDtoAdapter(carpooler);

    CarpoolerDto result = adapter.convert();

    assertEquals("Alice", result.name());
  }

}
