package org.carpooling.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTripsUseCaseTest {

  public static final Carpooler ALICE = new Carpooler("alice");
  public static final Carpooler BOB = new Carpooler("bob");
  public static final Carpooler CHARLIE = new Carpooler("charlie");
  private static final LocalDate DAY1 = LocalDate.parse("2015-10-21");
  private static final LocalDate DAY2 = LocalDate.parse("2015-10-22");
  private ListTripsUseCase listTripsUseCase;

  @BeforeEach
  void setUp() {
    InMemoryTripRepository tripRepository = new InMemoryTripRepository();
    tripRepository.add(new Trip(DAY1, ALICE, Set.of(BOB, CHARLIE)));
    tripRepository.add(new Trip(DAY2, BOB, Set.of(ALICE)));
    listTripsUseCase = new ListTripsUseCase(tripRepository);
  }

  @Test
  void shouldReturnAllTrips() {
    List<Trip> result = listTripsUseCase.execute();

    assertThat(result)
      .hasSize(2)
      .containsExactlyInAnyOrder(
        new Trip(DAY1, ALICE, Set.of(BOB, CHARLIE)),
        new Trip(DAY2, BOB, Set.of(ALICE))
      );
  }

}
