package org.carpooling.database.json;

import org.carpooling.domain.Carpooler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


class JsonFileCarpoolerRepositoryTest {

  private File file;

  @BeforeEach
  void setUp() throws IOException {
    file = File.createTempFile("JsonFileCarpoolerRepositoryTest", ".json");
  }

  @AfterEach
  void tearDown() {
    //noinspection ResultOfMethodCallIgnored
    file.delete();
  }

  @Test
  void givenNull_whenAdd_shouldThrowException() throws FileDatabaseNotFoundException {
    JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

    IllegalArgumentException exception =
      assertThrows(IllegalArgumentException.class, () -> repository.add(null));

    assertEquals("Carpooler cannot be null", exception.getMessage());
  }

  @Test
  void givenEmptyName_whenAdd_shouldThrowException() throws FileDatabaseNotFoundException {
    JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

    IllegalArgumentException exception;
    exception = assertThrows(IllegalArgumentException.class, () -> repository.add(new Carpooler(null)));
    assertEquals("Carpooler name cannot be empty", exception.getMessage());
    exception = assertThrows(IllegalArgumentException.class, () -> repository.add(new Carpooler("")));
    assertEquals("Carpooler name cannot be empty", exception.getMessage());
    exception = assertThrows(IllegalArgumentException.class, () -> repository.add(new Carpooler("  ")));
    assertEquals("Carpooler name cannot be empty", exception.getMessage());
  }

  @Test
  void givenNull_whenFindByName_shouldThrowException() throws FileDatabaseNotFoundException {
    JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

    IllegalArgumentException exception =
      assertThrows(IllegalArgumentException.class, () -> repository.findByName(null));

    assertEquals("Carpooler cannot be null", exception.getMessage());
  }

  @Test
  void givenNonExistingCarpooler_whenFindByName_shouldThrowException() throws FileDatabaseNotFoundException {
    JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

    FileDatabaseException exception =
      assertThrows(FileDatabaseException.class, () -> repository.findByName("foobar"));

    assertEquals("Carpooler [foobar] not found", exception.getMessage());
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
      void givenCarpooler_shouldWrite() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

        repository.add(new Carpooler("Alice"));

        Set<Carpooler> carpoolers = repository.findAll();
        assertEquals(1, carpoolers.size());
        assertThat(carpoolers, contains(new Carpooler("Alice")));
      }
    }

    @Nested
    class FindAllTest {
      @Test
      void shouldReturnEmptySet() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);
        Set<Carpooler> result = repository.findAll();

        assertThat(result.size(), is(0));
      }
    }

  }

  @Nested
  class WithInitializedDatabaseTest {

    @BeforeEach
    void setUp() throws IOException {
      String initialContent = """
        [
          {"name": "Alice"},
          {"name": "Bob"},
          {"name": "Charlie"}
        ]""";

      FileWriter writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class AddTest {
      @Test
      void givenExistingCarpooler_shouldThrowException() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

        IllegalArgumentException exception =
          assertThrows(IllegalArgumentException.class, () -> repository.add(new Carpooler("Alice")));

        assertEquals("Entry [Alice] already exists", exception.getMessage());
      }
    }

    @Nested
    class FindByNameTest {
      @Test
      void givenExistingCarpooler_shouldReturnCarpooler() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

        Carpooler result = repository.findByName("Alice");

        assertNotNull(result);
        assertEquals(new Carpooler("Alice"), result);
      }
    }

    @Nested
    class FindAllTest {
      @Test
      void shouldReturnAllCarpoolers() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);
        Set<Carpooler> result = repository.findAll();

        assertThat(result.size(), is(3));
        assertThat(result,
          containsInAnyOrder(
            new Carpooler("Alice"),
            new Carpooler("Bob"),
            new Carpooler("Charlie")
          )
        );
      }
    }

  }

  @Nested
  class WithDuplicatedEntriesTest {

    @BeforeEach
    void setUp() throws IOException {
      String initialContent = """
        [
          {"name": "Alice"},
          {"name": "Alice"},
          {"name": "Bob"},
          {"name": "Charlie"}
        ]""";

      FileWriter writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class FindByNameTest {
      @Test
      void givenTwoOrMoreMatches_shouldThrowException() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

        FileDatabaseException exception =
          assertThrows(FileDatabaseException.class, () -> repository.findByName("Alice"));

        assertEquals("Duplicated entry has been found", exception.getMessage());
      }
    }

    @Nested
    class FindAllTest {
      @Test
      void givenTwoOrMoreMatches_shouldThrowException() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

        FileDatabaseException exception =
          assertThrows(FileDatabaseException.class, repository::findAll);

        assertEquals("Duplicated entry has been found", exception.getMessage());
      }
    }

  }

}
