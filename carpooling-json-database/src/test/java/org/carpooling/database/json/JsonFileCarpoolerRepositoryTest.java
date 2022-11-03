package org.carpooling.database.json;

import org.carpooling.domain.Carpooler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


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
    var repository = new JsonFileCarpoolerRepository(file);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> repository.add(null))
      .withMessage("Carpooler cannot be null");
  }

  @Test
  void givenEmptyId_whenAdd_shouldThrowException() throws FileDatabaseNotFoundException {
    var repository = new JsonFileCarpoolerRepository(file);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> repository.add(new Carpooler(null)))
      .withMessage("Carpooler id cannot be empty");
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> repository.add(new Carpooler("")))
      .withMessage("Carpooler id cannot be empty");
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> repository.add(new Carpooler("  ")))
      .withMessage("Carpooler id cannot be empty");
  }

  @Test
  void givenNull_whenFindById_shouldThrowException() throws FileDatabaseNotFoundException {
    var repository = new JsonFileCarpoolerRepository(file);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> repository.findById(null))
      .withMessage("Carpooler cannot be null");
  }

  @Test
  void givenNonExistingCarpooler_whenFindById_shouldThrowException() throws FileDatabaseNotFoundException {
    var repository = new JsonFileCarpoolerRepository(file);

    assertThatExceptionOfType(FileDatabaseException.class)
      .isThrownBy(() -> repository.findById("foobar"))
      .withMessage("Carpooler [foobar] not found");
  }

  @Nested
  class WithEmptyDatabaseTest {

    @BeforeEach
    void setUp() throws IOException {
      var initialContent = "";

      var writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class AddTest {
      @Test
      void givenCarpooler_shouldWrite() throws FileDatabaseNotFoundException {
        var repository = new JsonFileCarpoolerRepository(file);

        repository.add(new Carpooler("alice"));

        var carpoolers = repository.findAll();
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
        var repository = new JsonFileCarpoolerRepository(file);
        var result = repository.findAll();

        assertThat(result).isNotNull().isEmpty();
      }
    }

  }

  @Nested
  class WithInitializedDatabaseTest {

    @BeforeEach
    void setUp() throws IOException {
      var initialContent = """
        [
          {"id": "alice"},
          {"id": "bob"},
          {"id": "charlie"}
        ]""";

      var writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class AddTest {
      @Test
      void givenExistingCarpooler_shouldThrowException() throws FileDatabaseNotFoundException {
        var repository = new JsonFileCarpoolerRepository(file);

        assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> repository.add(new Carpooler("alice")))
          .withMessage("Entry [alice] already exists");
      }
    }

    @Nested
    class FindByIdTest {
      @Test
      void givenExistingCarpooler_shouldReturnCarpooler() throws FileDatabaseNotFoundException {
        var repository = new JsonFileCarpoolerRepository(file);

        var result = repository.findById("alice");

        assertThat(result).isEqualTo(new Carpooler("alice"));
      }
    }

    @Nested
    class FindAllTest {
      @Test
      void shouldReturnAllCarpoolers() throws FileDatabaseNotFoundException {
        var repository = new JsonFileCarpoolerRepository(file);
        var result = repository.findAll();

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
      var initialContent = """
        [
          {"id": "alice"},
          {"id": "alice"},
          {"id": "bob"},
          {"id": "charlie"}
        ]""";

      var writer = new FileWriter(file);
      writer.write(initialContent);
      writer.close();
    }

    @Nested
    class FindByIdTest {
      @Test
      void givenTwoOrMoreMatches_shouldThrowException() throws FileDatabaseNotFoundException {
        var repository = new JsonFileCarpoolerRepository(file);

        assertThatExceptionOfType(FileDatabaseException.class)
          .isThrownBy(() -> repository.findById("alice"))
          .withMessage("Duplicated entry has been found");
      }
    }

    @Nested
    class FindAllTest {
      @Test
      void givenTwoOrMoreMatches_shouldThrowException() throws FileDatabaseNotFoundException {
        var repository = new JsonFileCarpoolerRepository(file);

        assertThatExceptionOfType(FileDatabaseException.class)
          .isThrownBy(repository::findAll)
          .withMessage("Duplicated entry has been found");
      }
    }

  }

}
