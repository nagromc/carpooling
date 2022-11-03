package org.carpooling.database.json.adapter;

import org.carpooling.database.json.FileDatabaseException;
import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TripDtoAdapterTest {

  @Test
  void givenNull_shouldThrowException() {
    assertThatExceptionOfType(FileDatabaseException.class)
      .isThrownBy(() -> new TripDtoAdapter(null))
      .withMessage("Domain object cannot be null");
  }

  @Test
  void givenTrip_shouldConvert() {
    Trip domainTrip = new Trip(
      LocalDate.parse("2015-10-21"),
      new Carpooler("alice"),
      Set.of(new Carpooler("bob"), new Carpooler("charlie"))
    );
    TripDtoAdapter adapter = new TripDtoAdapter(domainTrip);

    TripDto result = adapter.convert();

    assertThat(result.date).isEqualTo("2015-10-21");
    assertThat(result.driverId).isEqualTo("alice");
    assertThat(result.passengersId).containsExactlyInAnyOrder("bob", "charlie");
  }

}
