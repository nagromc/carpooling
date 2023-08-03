package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TripDtoAdapterTest {

  public static final Carpooler ALICE = new Carpooler("alice");
  public static final Carpooler BOB = new Carpooler("bob");
  public static final Carpooler CHARLIE = new Carpooler("charlie");
  public static final Carpooler DAVID = new Carpooler("david");

  @Test
  void givenNull_shouldThrowException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new TripDtoAdapter(null))
      .withMessage("Trip cannot be null");
  }

  @Test
  void shouldReturnDto() {
  var trip = new Trip(LocalDate.parse("2015-10-21"), Set.of(ALICE, DAVID), Set.of(BOB, CHARLIE));
    var adapter = new TripDtoAdapter(trip);

    var result = adapter.convert();

    assertThat(result.date()).isEqualTo(LocalDate.parse("2015-10-21"));
    assertThat(result.drivers()).containsExactlyInAnyOrder("alice", "david");
    assertThat(result.passengers()).containsExactlyInAnyOrder("bob", "charlie");
  }

}
