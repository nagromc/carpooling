package org.carpooling.database.json.adapter;

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
    assertThrows(IllegalArgumentException.class, () -> new TripDtoAdapter(null));
  }

  @Test
  void givenTrip_shouldConvert() {
    Trip domainTrip = new Trip(
      new Carpooler("Alice"),
      Set.of(new Carpooler("Bob"), new Carpooler("Charlie"))
    );
    TripDtoAdapter adapter = new TripDtoAdapter(domainTrip);

    TripDto result = adapter.convert();

    assertEquals("Alice", result.driverName);
    assertThat(result.passengersName, contains("Bob", "Charlie"));
  }

}
