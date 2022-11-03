package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.rest.dto.CarpoolerDto;
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
    CarpoolerDtoAdapter adapter = new CarpoolerDtoAdapter(new Carpooler("alice"));

    CarpoolerDto result = adapter.convert();

    assertThat(result.id()).isEqualTo("alice");
  }

}
