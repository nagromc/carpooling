package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.carpooling.rest.dto.TripDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TripDtoAdapterTest {

  public static final Carpooler ALICE = new Carpooler("alice");
  public static final Carpooler BOB = new Carpooler("bob");
  public static final Carpooler CHARLIE = new Carpooler("charlie");

  @Test
  void givenNull_shouldThrowException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TripDtoAdapter(null));
    assertThat(exception.getMessage()).isEqualTo("Trip cannot be null");
  }

  @Test
  void shouldReturnDto() {
    Trip trip = new Trip(LocalDate.parse("2015-10-21"), ALICE, Set.of(BOB, CHARLIE));
    TripDtoAdapter adapter = new TripDtoAdapter(trip);

    TripDto result = adapter.convert();

    assertThat(result.date()).isEqualTo(LocalDate.parse("2015-10-21"));
    assertThat(result.driver()).isEqualTo("alice");
    assertThat(result.passengers()).containsExactlyInAnyOrder("bob", "charlie");
  }

}
