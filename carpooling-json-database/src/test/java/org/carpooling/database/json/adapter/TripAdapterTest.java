package org.carpooling.database.json.adapter;

import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TripAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> new TripAdapter(null));
  }

  @Test
  void givenTripDto_shouldReturnTrip() {
    TripDto tripDto = new TripDto("Alice", Set.of("Bob", "Charlie"));
    TripAdapter adapter = new TripAdapter(tripDto);

    Trip result = adapter.convert();

    assertEquals("Alice", result.driver().name());
    assertThat(
      result.passengers().stream().map(Carpooler::name).collect(Collectors.toSet()),
      contains("Bob", "Charlie")
    );
  }

}
