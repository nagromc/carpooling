package org.carpooling.database.json.adapter;

import org.assertj.core.api.Assertions;
import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.domain.Carpooler;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CarpoolerDtoAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    Assertions.assertThatExceptionOfType(FileDatabaseException.class)
      .isThrownBy(() -> new CarpoolerDtoAdapter(null))
      .withMessage("DTO cannot be null");
  }

  @Test
  void givenCarpooler_shouldConvert() {
    var carpooler = new Carpooler("alice", "Alice");
    var adapter = new CarpoolerDtoAdapter(carpooler);

    var result = adapter.convert();

    assertThat(result.id).isEqualTo("alice");
    assertThat(result.displayName).isEqualTo("Alice");
  }

}
