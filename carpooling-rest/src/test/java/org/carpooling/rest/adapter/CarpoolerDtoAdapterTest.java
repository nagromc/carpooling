package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.rest.dto.CarpoolerDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarpoolerDtoAdapterTest {

  @Test
  void givenNull_ShouldThrowException() {
    IllegalArgumentException exception =
      assertThrows(IllegalArgumentException.class, () -> new CarpoolerDtoAdapter(null));
    assertThat(exception.getMessage(), is("Carpooler cannot be null"));
  }

  @Test
  void shouldReturnCarpoolerDto() {
    CarpoolerDtoAdapter adapter = new CarpoolerDtoAdapter(new Carpooler("alice"));

    CarpoolerDto result = adapter.convert();

    assertThat(result.id(), is("alice"));
  }

}
