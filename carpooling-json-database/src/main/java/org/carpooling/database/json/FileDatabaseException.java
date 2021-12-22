package org.carpooling.database.json;

public class FileDatabaseException extends RuntimeException {

  public FileDatabaseException(String message) {
    super(message);
  }

  public FileDatabaseException(Exception e) {
    super(e);
  }

}
