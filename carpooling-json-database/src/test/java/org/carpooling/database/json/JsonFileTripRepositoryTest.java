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
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonFileTripRepositoryTest {

  private static final Carpooler ALICE_CARPOOLER = new Carpooler("Alice");
  private static final Carpooler BOB_CARPOOLER = new Carpooler("Bob");
  private static final Carpooler CHARLIE_CARPOOLER = new Carpooler("Charlie");
  private static final Carpooler DAVID_CARPOOLER = new Carpooler("David");
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
  void givenNotExistingFile_shouldThrowException() {
    assertThrows(FileDatabaseNotFoundException.class,
      () -> new JsonFileTripRepository(new File("/not/existing/path")));
  }

  @Test
  void givenExistingFile_shouldNotThrowException() {
    assertDoesNotThrow(() -> new JsonFileTripRepository(new File("src/test/resources/dummy-database.json")));
  }

  @Nested
  class AddTest {

    @Test
    void givenTrip_shouldSave() throws FileDatabaseNotFoundException {
      JsonFileTripRepository repository = new JsonFileTripRepository(file);

      repository.add(new Trip(ALICE_CARPOOLER, Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER)));

      List<Trip> trips = repository.findAll();
      assertThat(trips.size(), is(1));
      assertThat(trips.get(0).driver(), is(ALICE_CARPOOLER));
      assertThat(trips.get(0).passengers(), is(Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER)));
    }

    @Test
    void givenThreeTrips_shouldSave() throws FileDatabaseNotFoundException {
      JsonFileTripRepository repository = new JsonFileTripRepository(file);

      repository.add(new Trip(ALICE_CARPOOLER, Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER)));
      repository.add(new Trip(BOB_CARPOOLER, Set.of(DAVID_CARPOOLER)));
      repository.add(new Trip(DAVID_CARPOOLER, Set.of(ALICE_CARPOOLER, BOB_CARPOOLER, CHARLIE_CARPOOLER)));

      List<Trip> trips = repository.findAll();
      assertThat(trips.size(), is(3));

      Trip trip1 = trips.get(0);
      assertThat(trip1.driver(), is(ALICE_CARPOOLER));
      assertThat(trip1.passengers(), is(Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER)));

      Trip trip2 = trips.get(1);
      assertThat(trip2.driver(), is(BOB_CARPOOLER));
      assertThat(trip2.passengers(), is(Set.of(DAVID_CARPOOLER)));

      Trip trip3 = trips.get(2);
      assertThat(trip3.driver(), is(DAVID_CARPOOLER));
      assertThat(trip3.passengers(), is(Set.of(ALICE_CARPOOLER, BOB_CARPOOLER, CHARLIE_CARPOOLER)));
    }

  }

  @Nested
  class FindAllTest {

    @Nested
    class WithEmptyDatabaseTest {

      @BeforeEach
      void setUp() throws IOException {
        String initialContent = "";

        FileWriter writer = new FileWriter(file);
        writer.write(initialContent);
        writer.close();
      }

      @Test
      void givenDatabaseIsEmpty_shouldReturnEmptyList() throws FileDatabaseNotFoundException {
        JsonFileTripRepository repository = new JsonFileTripRepository(file);

        List<Trip> allTrips = repository.findAll();

        assertThat(allTrips.size(), is(0));
      }

    }

    @BeforeEach
    void setUp() throws IOException {
      String initialContent = """
        [
          {
            "driver": "Alice",
            "passengers": [
              "Bob",
              "Charlie"
            ]
          },
          {
            "driver": "Bob",
            "passengers": [
              "Alice",
              "Charlie"
            ]
          }
        ]""";

      FileWriter writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Test
    void shouldReturnAllTrips() throws FileDatabaseNotFoundException {
      JsonFileTripRepository repository = new JsonFileTripRepository(file);

      List<Trip> allTrips = repository.findAll();

      Trip expectedTrip1 = new Trip(ALICE_CARPOOLER, Set.of(BOB_CARPOOLER, CHARLIE_CARPOOLER));
      Trip expectedTrip2 = new Trip(BOB_CARPOOLER, Set.of(ALICE_CARPOOLER, CHARLIE_CARPOOLER));
      assertThat(allTrips, is(equalTo(List.of(expectedTrip1, expectedTrip2))));
    }

  }

}
