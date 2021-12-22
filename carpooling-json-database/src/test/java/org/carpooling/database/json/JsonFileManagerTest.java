package org.carpooling.database.json;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

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
    assertThrows(FileDatabaseNotFoundException.class,
      () -> new JsonFileManager(new File("/not/existing/path")));
  }

  @Test
  void givenExistingFile_shouldNotThrowException() {
    assertDoesNotThrow(() -> new JsonFileManager(new File("src/test/resources/dummy-file.json")));
  }

  @Test
  void shouldWrite() throws FileDatabaseNotFoundException, IOException {
    JsonFileManager fileManager = new JsonFileManager(file);

    fileManager.write("foobar");

    String result = Files.readString(file.toPath());
    assertEquals("foobar", result);
  }

  @Test
  void shouldRead() throws FileDatabaseNotFoundException, IOException {
    JsonFileManager fileManager = new JsonFileManager(file);
    Files.writeString(file.toPath(), "foobar");

    String result = fileManager.read();

    assertEquals("foobar", result);
  }

}
