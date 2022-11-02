package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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
    TripDto tripDto = new TripDto("2015-10-21", "alice", Set.of("bob", "charlie"));
    TripAdapter adapter = new TripAdapter(tripDto);

    Trip result = adapter.convert();

    assertThat(result.date()).isEqualTo(LocalDate.parse("2015-10-21"));
    assertEquals("alice", result.driver().id());
    assertThat(result.passengers()).map(Carpooler::id).contains("bob", "charlie");
  }

}
