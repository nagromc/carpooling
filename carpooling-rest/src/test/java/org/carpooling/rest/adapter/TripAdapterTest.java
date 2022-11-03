package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.rest.dto.TripDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TripAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new TripAdapter(null))
      .withMessage("TripDto cannot be null");
  }

  @Test
  void shouldReturnTrip() {
    var tripDto = new TripDto(LocalDate.parse("2015-10-21"), "alice", Set.of("bob", "charlie"));

    var result = new TripAdapter(tripDto).convert();

    assertThat(result.date()).isEqualTo(LocalDate.parse("2015-10-21"));
    assertThat(result.driver()).isEqualTo(new Carpooler("alice"));
    assertThat(result.passengers()).isEqualTo(Set.of(new Carpooler("bob"), new Carpooler("charlie")));
  }

}
