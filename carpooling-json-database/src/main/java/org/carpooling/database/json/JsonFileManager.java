package org.carpooling.database.json;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JsonFileManager {

  private final File file;

  public JsonFileManager(File file) throws FileDatabaseNotFoundException {
    validateParameters(file);
    this.file = file;
  }

  void validateParameters(File file) throws FileDatabaseNotFoundException {
    if (!file.exists())
      throw new FileDatabaseNotFoundException(String.format("File [%s] not found", file.getAbsolutePath()));
  }

  String read() {
    try {
      return Files.readString(this.file.toPath());
    } catch (IOException e) {
      throw new FileDatabaseException(e);
    }
  }

  void write(String content) {
    try {
      Files.writeString(file.toPath(), content);
    } catch (IOException e) {
      throw new FileDatabaseException(e);
    }
  }

}
