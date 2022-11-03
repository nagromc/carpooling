package org.carpooling.database.json;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class JsonFileManagerTest {

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
    Assertions.assertThatExceptionOfType(FileDatabaseNotFoundException.class)
      .isThrownBy(() -> new JsonFileManager(new File("/not/existing/path")));
  }

  @Test
  void givenExistingFile_shouldNotThrowException() {
    assertThatNoException()
      .isThrownBy(() -> new JsonFileManager(new File("src/test/resources/dummy-file.json")));
  }

  @Test
  void shouldWrite() throws FileDatabaseNotFoundException, IOException {
    JsonFileManager fileManager = new JsonFileManager(file);

    fileManager.write("foobar");

    String result = Files.readString(file.toPath());
    assertThat(result).isEqualTo("foobar");
  }

  @Test
  void shouldRead() throws FileDatabaseNotFoundException, IOException {
    JsonFileManager fileManager = new JsonFileManager(file);
    Files.writeString(file.toPath(), "foobar");

    String result = fileManager.read();

    assertThat(result).isEqualTo("foobar");
  }

}
