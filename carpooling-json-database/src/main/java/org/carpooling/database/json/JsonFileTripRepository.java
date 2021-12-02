package org.carpooling.database.json;

import com.google.gson.Gson;
import org.carpooling.Carpooler;
import org.carpooling.Trip;
import org.carpooling.TripRepository;
import org.carpooling.database.json.adapter.TripAdapter;
import org.carpooling.database.json.dto.TripDto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class JsonFileTripRepository implements TripRepository {

  private final File databaseFile;
  private final String content;
  private final Gson gson;

  public JsonFileTripRepository(File databaseFile) throws FileDatabaseNotFoundException {
    gson = new Gson();
    this.databaseFile = databaseFile;

    validateParameters();

    content = loadDatabase(this.databaseFile);
  }

  private String loadDatabase(File file) {
    try {
      return Files.readString(file.toPath());
    } catch (IOException e) {
      throw new FileDatabaseException(e);
    }
  }

  private void validateParameters() throws FileDatabaseNotFoundException {
    if (!databaseFile.exists())
      throw new FileDatabaseNotFoundException(String.format("File [%s] not found", databaseFile.getPath()));
  }

  @Override
  public void add(Carpooler driver, Set<Carpooler> passengers) {
    throw new RuntimeException("Implement me");
  }

  @Override
  public List<Trip> findAll() {
    TripDto[] tripDtos = gson.fromJson(content, TripDto[].class);

    return Arrays.stream(tripDtos)
      .map(tripDto -> new TripAdapter(tripDto).convert())
      .toList();
  }

}
