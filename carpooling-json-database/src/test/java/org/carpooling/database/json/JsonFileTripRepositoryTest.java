package org.carpooling.database.json;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.Trip;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonFileTripRepositoryTest {

  private static final Carpooler ALICE_CARPOOLER = new Carpooler("alice");
  private static final Carpooler BOB_CARPOOLER = new Carpooler("bob");
  private static final Carpooler CHARLIE_CARPOOLER = new Carpooler("charlie");
  private static final Carpooler DAVID_CARPOOLER = new Carpooler("David");
  public static final LocalDate DAY1 = LocalDate.parse("2015-10-21");
  public static final LocalDate DAY2 = LocalDate.parse("2015-10-22");
  public static final LocalDate DAY3 = LocalDate.parse("2015-10-23");
  private File file;

  @BeforeEach
  void setUp() throws IOException {
    file = File.createTempFile("JsonFileTripRepositoryTest", ".json");
  }

  @AfterEach
  void tearDown() {
    //noinspection ResultOfMethodCallIgnored
    file.delete();
  }

  @Test
  void givenNull_shouldThrowException() throws FileDatabaseNotFoundException {
    JsonFileTripRepository repository = new JsonFileTripRepository(file);

    FileDatabaseException exception = assertThrows(FileDatabaseException.class, () -> repository.add(null));
    assertThat(exception.getMessage()).isEqualTo("Cannot save null trip");
  }

  @Nested
  class WithEmptyDatabaseTest {

    @BeforeEach
    void setUp() throws IOException {
      String initialContent = "";

      FileWriter writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class AddTest {
      @Test
      void givenTrip_shouldSave() throws FileDatabaseNotFoundException {
        JsonFileTripRepository repository = new JsonFileTripRepository(file);

        repository.add(new Trip(DAY1, ALICE_CARPOOLER, Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER)));

        List<Trip> trips = repository.findAll();
        assertThat(trips).isNotNull().hasSize(1);
        Trip onlyTrip = trips.get(0);
        assertThat(onlyTrip.date()).isEqualTo(DAY1);
        assertThat(onlyTrip.driver()).isEqualTo(ALICE_CARPOOLER);
        assertThat(onlyTrip.passengers()).isEqualTo(Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER));
      }

      @Test
      void givenThreeTrips_shouldSave() throws FileDatabaseNotFoundException {
        JsonFileTripRepository repository = new JsonFileTripRepository(file);

        repository.add(new Trip(DAY1, ALICE_CARPOOLER, Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER)));
        repository.add(new Trip(DAY2, BOB_CARPOOLER, Set.of(DAVID_CARPOOLER)));
        repository.add(new Trip(DAY3, DAVID_CARPOOLER, Set.of(ALICE_CARPOOLER, BOB_CARPOOLER, CHARLIE_CARPOOLER)));

        List<Trip> trips = repository.findAll();
        assertThat(trips).isNotNull().hasSize(3);

        Trip trip1 = trips.get(0);
        assertThat(trip1.date()).isEqualTo(DAY1);
        assertThat(trip1.driver()).isEqualTo(ALICE_CARPOOLER);
        assertThat(trip1.passengers()).isEqualTo(Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER));

        Trip trip2 = trips.get(1);
        assertThat(trip2.date()).isEqualTo(DAY2);
        assertThat(trip2.driver()).isEqualTo(BOB_CARPOOLER);
        assertThat(trip2.passengers()).isEqualTo(Set.of(DAVID_CARPOOLER));

        Trip trip3 = trips.get(2);
        assertThat(trip3.date()).isEqualTo(DAY3);
        assertThat(trip3.driver()).isEqualTo(DAVID_CARPOOLER);
        assertThat(trip3.passengers()).isEqualTo(Set.of(ALICE_CARPOOLER, BOB_CARPOOLER, CHARLIE_CARPOOLER));
      }
    }

    @Nested
    class FindAllTest {
      @Test
      void givenDatabaseIsEmpty_shouldReturnEmptyList() throws FileDatabaseNotFoundException {
        JsonFileTripRepository repository = new JsonFileTripRepository(file);

        List<Trip> allTrips = repository.findAll();

        assertThat(allTrips).isNotNull().isEmpty();
      }
    }

  }

  @Nested
  class WithInitializedDatabaseTest {

    @BeforeEach
    void setUp() throws IOException {
      String initialContent = """
        [
          {
            "date": "2015-10-21",
            "driver": "alice",
            "passengers": [
              "bob",
              "charlie"
            ]
          },
          {
            "date": "2015-10-22",
            "driver": "bob",
            "passengers": [
              "alice",
              "charlie"
            ]
          }
        ]""";

      FileWriter writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class FindAllTest {

      @Test
      void shouldReturnAllTrips() throws FileDatabaseNotFoundException {
        JsonFileTripRepository repository = new JsonFileTripRepository(file);

        List<Trip> allTrips = repository.findAll();

        Trip expectedTrip1 = new Trip(DAY1, ALICE_CARPOOLER, Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER));
        Trip expectedTrip2 = new Trip(DAY2, BOB_CARPOOLER, Set.of(ALICE_CARPOOLER, CHARLIE_CARPOOLER));
        assertThat(allTrips).isEqualTo(List.of(expectedTrip1, expectedTrip2));
      }

    }
  }

}
