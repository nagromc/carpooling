package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Carpooler;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TripAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    assertThatExceptionOfType(FileDatabaseException.class)
      .isThrownBy(() -> new TripAdapter(null))
      .withMessage("Domain object cannot be null");
  }

  @Test
  void givenTripDto_shouldReturnTrip() {
    var tripDto = new TripDto("2015-10-21", Set.of("alice", "david"), Set.of("bob", "charlie"));
    var adapter = new TripAdapter(tripDto);

    var result = adapter.convert();

    assertThat(result.date()).isEqualTo(LocalDate.parse("2015-10-21"));
    assertThat(result.drivers()).map(Carpooler::id).containsExactlyInAnyOrder("alice", "david");
    assertThat(result.passengers()).map(Carpooler::id).containsExactlyInAnyOrder("bob", "charlie");
  }

}
