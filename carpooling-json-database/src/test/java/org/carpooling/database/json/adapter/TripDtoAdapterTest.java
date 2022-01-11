package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TripDtoAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    FileDatabaseException exception = assertThrows(FileDatabaseException.class, () -> new TripDtoAdapter(null));
    assertEquals("Domain object cannot be null", exception.getMessage());
  }

  @Test
  void givenTrip_shouldConvert() {
    Trip domainTrip = new Trip(
      new Carpooler("alice"),
      Set.of(new Carpooler("bob"), new Carpooler("charlie"))
    );
    TripDtoAdapter adapter = new TripDtoAdapter(domainTrip);

    TripDto result = adapter.convert();

    assertEquals("alice", result.driverId);
    assertThat(result.passengersId, contains("bob", "charlie"));
  }

}
