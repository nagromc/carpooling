package org.carpooling.rest.adapter;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.carpooling.rest.dto.TripDto;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TripAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TripAdapter(null));
    assertThat(exception.getMessage(), is("TripDto cannot be null"));
  }

  @Test
  void shouldReturnTrip() {
    TripDto tripDto = new TripDto("alice", Set.of("bob", "charlie"));

    Trip result = new TripAdapter(tripDto).convert();

    assertThat(result.driver(), is(new Carpooler("alice")));
    assertThat(result.passengers(), is(Set.of(new Carpooler("bob"), new Carpooler("charlie"))));
  }

}
