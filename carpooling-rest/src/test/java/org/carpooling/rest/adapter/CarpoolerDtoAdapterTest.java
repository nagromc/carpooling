package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CarpoolerDtoAdapterTest {

  @Test
  void givenNull_ShouldThrowException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new CarpoolerDtoAdapter(null))
      .withMessage("Carpooler cannot be null");
  }

  @Test
  void shouldReturnCarpoolerDto() {
    var adapter = new CarpoolerDtoAdapter(new Carpooler("alice"));

    var result = adapter.convert();

    assertThat(result.id()).isEqualTo("alice");
  }

}
