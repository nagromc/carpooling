package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
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
    FileDatabaseException exception = assertThrows(FileDatabaseException.class, () -> new TripAdapter(null));
    assertEquals("Domain object cannot be null", exception.getMessage());
  }

  @Test
  void givenTripDto_shouldReturnTrip() {
    TripDto tripDto = new TripDto("alice", Set.of("bob", "charlie"));
    TripAdapter adapter = new TripAdapter(tripDto);

    Trip result = adapter.convert();

    assertEquals("alice", result.driver().id());
    assertThat(
      result.passengers().stream().map(Carpooler::id).collect(Collectors.toSet()),
      contains("bob", "charlie")
    );
  }

}
