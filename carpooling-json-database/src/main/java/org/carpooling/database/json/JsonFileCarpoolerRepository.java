package org.carpooling.database.json;

import com.google.gson.Gson;
import org.carpooling.database.json.adapter.CarpoolerAdapter;
import org.carpooling.database.json.adapter.CarpoolerDtoAdapter;
import org.carpooling.database.json.dto.CarpoolerDto;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.CarpoolerRepository;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonFileCarpoolerRepository implements CarpoolerRepository {

  private final Gson gson;
  private final JsonFileManager fileManager;

  public JsonFileCarpoolerRepository(File databaseFile) throws FileDatabaseNotFoundException {
    gson = new Gson();
    fileManager = new JsonFileManager(databaseFile);
  }

  @Override
  public void add(Carpooler carpooler) {
    validateAddArguments(carpooler);

    var carpoolers = findAll();

    var foundExistingEntry = !carpoolers.add(carpooler);
    if (foundExistingEntry)
      throw new IllegalArgumentException("Entry [%s] already exists".formatted(carpooler.id()));

    var dtos = carpoolers.stream()
      .map(c -> new CarpoolerDtoAdapter(c).convert())
      .collect(Collectors.toSet());

    var json = gson.toJson(dtos);
    fileManager.write(json);
  }

  private void validateAddArguments(Carpooler carpooler) {
    if (carpooler == null)
      throw new IllegalArgumentException("Carpooler cannot be null");

    if (carpooler.id() == null || carpooler.id().isBlank())
      throw new IllegalArgumentException("Carpooler id cannot be empty");
  }

  @Override
  public Carpooler findById(String id) {
    if (id == null)
      throw new IllegalArgumentException("Carpooler cannot be null");

    var foundCarpooler = findCarpoolerById(id);

    if (foundCarpooler.isEmpty())
      throw new FileDatabaseException("Carpooler [%s] not found".formatted(id));

    return foundCarpooler.get();
  }

  private Optional<Carpooler> findCarpoolerById(String id) {
    var carpoolers = findAll();

    return carpoolers.stream()
      .filter(carpooler -> carpooler.id().equals(id))
      .findFirst();
  }

  @Override
  public Set<Carpooler> findAll() {
    var content = fileManager.read();
    var dtos = gson.fromJson(content, CarpoolerDto[].class);

    if (dtos == null)
      return new HashSet<>();

    if (areDuplicatedEntries(dtos))
      throw new FileDatabaseException("Duplicated entry has been found");

    return Arrays.stream(dtos)
      .map(dto -> new CarpoolerAdapter(dto).convert())
      .collect(Collectors.toSet());
  }

  private boolean areDuplicatedEntries(CarpoolerDto[] dtos) {
    var numberOfEntries = dtos.length;
    var numberOfUniqueEntries = Arrays.stream(dtos)
      .distinct()
      .count();
    return numberOfEntries != numberOfUniqueEntries;
  }

}
