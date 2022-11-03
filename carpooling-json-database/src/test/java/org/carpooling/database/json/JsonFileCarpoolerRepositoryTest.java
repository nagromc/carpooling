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

import static org.assertj.core.api.Assertions.assertThat;
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
  void givenEmptyId_whenAdd_shouldThrowException() throws FileDatabaseNotFoundException {
    JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

    IllegalArgumentException exception;
    exception = assertThrows(IllegalArgumentException.class, () -> repository.add(new Carpooler(null)));
    assertEquals("Carpooler id cannot be empty", exception.getMessage());
    exception = assertThrows(IllegalArgumentException.class, () -> repository.add(new Carpooler("")));
    assertEquals("Carpooler id cannot be empty", exception.getMessage());
    exception = assertThrows(IllegalArgumentException.class, () -> repository.add(new Carpooler("  ")));
    assertEquals("Carpooler id cannot be empty", exception.getMessage());
  }

  @Test
  void givenNull_whenFindById_shouldThrowException() throws FileDatabaseNotFoundException {
    JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

    IllegalArgumentException exception =
      assertThrows(IllegalArgumentException.class, () -> repository.findById(null));

    assertEquals("Carpooler cannot be null", exception.getMessage());
  }

  @Test
  void givenNonExistingCarpooler_whenFindById_shouldThrowException() throws FileDatabaseNotFoundException {
    JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

    FileDatabaseException exception =
      assertThrows(FileDatabaseException.class, () -> repository.findById("foobar"));

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

        repository.add(new Carpooler("alice"));

        Set<Carpooler> carpoolers = repository.findAll();
        assertThat(carpoolers)
          .hasSize(1)
          .map(Carpooler::id)
          .containsExactlyInAnyOrder("alice");
      }
    }

    @Nested
    class FindAllTest {
      @Test
      void shouldReturnEmptySet() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);
        Set<Carpooler> result = repository.findAll();

        assertThat(result).isNotNull().isEmpty();
      }
    }

  }

  @Nested
  class WithInitializedDatabaseTest {

    @BeforeEach
    void setUp() throws IOException {
      String initialContent = """
        [
          {"id": "alice"},
          {"id": "bob"},
          {"id": "charlie"}
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
          assertThrows(IllegalArgumentException.class, () -> repository.add(new Carpooler("alice")));

        assertEquals("Entry [alice] already exists", exception.getMessage());
      }
    }

    @Nested
    class FindByIdTest {
      @Test
      void givenExistingCarpooler_shouldReturnCarpooler() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

        Carpooler result = repository.findById("alice");

        assertNotNull(result);
        assertEquals(new Carpooler("alice"), result);
      }
    }

    @Nested
    class FindAllTest {
      @Test
      void shouldReturnAllCarpoolers() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);
        Set<Carpooler> result = repository.findAll();

        assertThat(result)
          .hasSize(3)
          .map(Carpooler::id)
          .containsExactlyInAnyOrder("alice", "bob", "charlie");
      }
    }

  }

  @Nested
  class WithDuplicatedEntriesTest {

    @BeforeEach
    void setUp() throws IOException {
      String initialContent = """
        [
          {"id": "alice"},
          {"id": "alice"},
          {"id": "bob"},
          {"id": "charlie"}
        ]""";

      FileWriter writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class FindByIdTest {
      @Test
      void givenTwoOrMoreMatches_shouldThrowException() throws FileDatabaseNotFoundException {
        JsonFileCarpoolerRepository repository = new JsonFileCarpoolerRepository(file);

        FileDatabaseException exception =
          assertThrows(FileDatabaseException.class, () -> repository.findById("alice"));

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
