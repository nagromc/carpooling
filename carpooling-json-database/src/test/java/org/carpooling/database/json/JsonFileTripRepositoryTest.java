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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class JsonFileTripRepositoryTest {

  private static final Carpooler ALICE_CARPOOLER = new Carpooler("alice");
  private static final Carpooler BOB_CARPOOLER = new Carpooler("bob");
  private static final Carpooler CHARLIE_CARPOOLER = new Carpooler("charlie");
  private static final Carpooler DAVID_CARPOOLER = new Carpooler("david");
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
    var repository = new JsonFileTripRepository(file);

    assertThatExceptionOfType(FileDatabaseException.class)
      .isThrownBy(() -> repository.add(null))
      .withMessage("Cannot save null trip");
  }

  @Nested
  class WithEmptyDatabase {

    @BeforeEach
    void setUp() throws IOException {
      var initialContent = "";

      var writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class Add {
      @Test
      void givenTrip_shouldSave() throws FileDatabaseNotFoundException {
        var repository = new JsonFileTripRepository(file);

        repository.add(new Trip(DAY1, Set.of(ALICE_CARPOOLER), Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER)));

        var trips = repository.findAll();
        assertThat(trips).isNotNull().hasSize(1);
        var onlyTrip = trips.get(0);
        assertThat(onlyTrip.date()).isEqualTo(DAY1);
        assertThat(onlyTrip.drivers()).isEqualTo(Set.of(ALICE_CARPOOLER));
        assertThat(onlyTrip.passengers()).isEqualTo(Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER));
      }

      @Test
      void givenThreeTrips_shouldSave() throws FileDatabaseNotFoundException {
        var repository = new JsonFileTripRepository(file);

        repository.add(new Trip(DAY1, Set.of(ALICE_CARPOOLER, DAVID_CARPOOLER), Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER)));
        repository.add(new Trip(DAY2, Set.of(BOB_CARPOOLER), Set.of(DAVID_CARPOOLER)));
        repository.add(new Trip(DAY3, Set.of(DAVID_CARPOOLER), Set.of(ALICE_CARPOOLER, BOB_CARPOOLER, CHARLIE_CARPOOLER)));

        var trips = repository.findAll();
        assertThat(trips).isNotNull().hasSize(3);

        var trip1 = trips.get(0);
        assertThat(trip1.date()).isEqualTo(DAY1);
        assertThat(trip1.drivers()).containsExactlyInAnyOrder(ALICE_CARPOOLER, DAVID_CARPOOLER);
        assertThat(trip1.passengers()).containsExactlyInAnyOrder(BOB_CARPOOLER, CHARLIE_CARPOOLER);

        var trip2 = trips.get(1);
        assertThat(trip2.date()).isEqualTo(DAY2);
        assertThat(trip2.drivers()).containsExactly(BOB_CARPOOLER);
        assertThat(trip2.passengers()).containsExactlyInAnyOrder(DAVID_CARPOOLER);

        var trip3 = trips.get(2);
        assertThat(trip3.date()).isEqualTo(DAY3);
        assertThat(trip3.drivers()).containsExactly(DAVID_CARPOOLER);
        assertThat(trip3.passengers()).containsExactlyInAnyOrder(ALICE_CARPOOLER, BOB_CARPOOLER, CHARLIE_CARPOOLER);
      }
    }

    @Nested
    class FindAll {
      @Test
      void givenDatabaseIsEmpty_shouldReturnEmptyList() throws FileDatabaseNotFoundException {
        var repository = new JsonFileTripRepository(file);

        var allTrips = repository.findAll();

        assertThat(allTrips).isNotNull().isEmpty();
      }
    }

  }

  @Nested
  class WithInitializedDatabase {

    @BeforeEach
    void setUp() throws IOException {
      // language=JSON
      var initialContent = """
        [
          {
            "date": "2015-10-21",
            "drivers": [
              "alice",
              "david"
            ],
            "passengers": [
              "bob",
              "charlie"
            ]
          },
          {
            "date": "2015-10-22",
            "drivers": ["bob"],
            "passengers": [
              "alice",
              "charlie"
            ]
          }
        ]""";

      var writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class FindAll {

      @Test
      void shouldReturnAllTrips() throws FileDatabaseNotFoundException {
        var repository = new JsonFileTripRepository(file);

        var allTrips = repository.findAll();

        var expectedTrip1 = new Trip(DAY1, Set.of(ALICE_CARPOOLER, DAVID_CARPOOLER), Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER));
        var expectedTrip2 = new Trip(DAY2, Set.of(BOB_CARPOOLER), Set.of(ALICE_CARPOOLER, CHARLIE_CARPOOLER));
        assertThat(allTrips).isEqualTo(List.of(expectedTrip1, expectedTrip2));
      }

    }
  }

}
