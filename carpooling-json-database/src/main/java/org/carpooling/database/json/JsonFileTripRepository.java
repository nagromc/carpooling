package org.carpooling.database.json;

import com.google.gson.Gson;
import org.carpooling.database.json.adapter.TripAdapter;
import org.carpooling.database.json.adapter.TripDtoAdapter;
import org.carpooling.database.json.dto.TripDto;
import org.carpooling.domain.Trip;
import org.carpooling.domain.TripRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonFileTripRepository implements TripRepository {

  private final JsonFileManager fileManager;
  private final Gson gson;

  public JsonFileTripRepository(File databaseFile) throws FileDatabaseNotFoundException {
    gson = new Gson();

    fileManager = new JsonFileManager(databaseFile);
  }

  @Override
  public void add(Trip trip) {
    validateArgument(trip);

    List<Trip> trips = findAll();

    trips.add(trip);

    List<TripDto> tripDtos = trips.stream()
      .map(t -> new TripDtoAdapter(t).convert())
      .toList();

    String content = gson.toJson(tripDtos);
    fileManager.write(content);
  }

  private void validateArgument(Trip trip) {
    if (trip == null) throw new FileDatabaseException("Cannot save null trip");
  }

  @Override
  public List<Trip> findAll() {
    String content = fileManager.read();
    TripDto[] tripDtos = gson.fromJson(content, TripDto[].class);

    if (tripDtos == null)
      return new ArrayList<>();

    return Arrays.stream(tripDtos)
      .map(tripDto -> new TripAdapter(tripDto).convert())
      .collect(Collectors.toList());
  }

}
