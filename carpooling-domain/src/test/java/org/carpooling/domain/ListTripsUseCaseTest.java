package org.carpooling.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class ListTripsUseCaseTest {

  public static final Carpooler ALICE = new Carpooler("alice");
  public static final Carpooler BOB = new Carpooler("bob");
  public static final Carpooler CHARLIE = new Carpooler("charlie");
  private ListTripsUseCase listTripsUseCase;

  @BeforeEach
  void setUp() {
    InMemoryTripRepository tripRepository = new InMemoryTripRepository();
    tripRepository.add(new Trip(ALICE, Set.of(BOB, CHARLIE)));
    tripRepository.add(new Trip(BOB, Set.of(ALICE)));
    listTripsUseCase = new ListTripsUseCase(tripRepository);
  }

  @Test
  void shouldReturnAllTrips() {
    List<Trip> result = listTripsUseCase.execute();

    assertThat(result.size(), is(2));
    assertThat(result, containsInAnyOrder(
      new Trip(ALICE, Set.of(BOB, CHARLIE)),
      new Trip(BOB, Set.of(ALICE))
    ));
  }

}
