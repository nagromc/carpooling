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

    Set<Carpooler> carpoolers = findAll();

    boolean foundExistingEntry = !carpoolers.add(carpooler);
    if (foundExistingEntry)
      throw new IllegalArgumentException("Entry [%s] already exists".formatted(carpooler.name()));

    Set<CarpoolerDto> dtos = carpoolers.stream()
      .map(c -> new CarpoolerDtoAdapter(c).convert())
      .collect(Collectors.toSet());

    String json = gson.toJson(dtos);
    fileManager.write(json);
  }

  private void validateAddArguments(Carpooler carpooler) {
    if (carpooler == null)
      throw new IllegalArgumentException("Carpooler cannot be null");

    if (carpooler.name() == null || carpooler.name().isBlank())
      throw new IllegalArgumentException("Carpooler name cannot be empty");
  }

  @Override
  public Carpooler findByName(String name) {
    if (name == null)
      throw new IllegalArgumentException("Carpooler cannot be null");

    Optional<Carpooler> foundCarpooler = findCarpoolerByName(name);

    if (foundCarpooler.isEmpty())
      throw new FileDatabaseException("Carpooler [%s] not found".formatted(name));

    return foundCarpooler.get();
  }

  private Optional<Carpooler> findCarpoolerByName(String name) {
    Set<Carpooler> carpoolers = findAll();

    return carpoolers.stream()
      .filter(carpooler -> carpooler.name().equals(name))
      .findFirst();
  }

  @Override
  public Set<Carpooler> findAll() {
    String content = fileManager.read();
    CarpoolerDto[] dtos = gson.fromJson(content, CarpoolerDto[].class);

    if (dtos == null)
      return new HashSet<>();

    if (areDuplicatedEntries(dtos))
      throw new FileDatabaseException("Duplicated entry has been found");

    return Arrays.stream(dtos)
      .map(dto -> new CarpoolerAdapter(dto).convert())
      .collect(Collectors.toSet());
  }

  private boolean areDuplicatedEntries(CarpoolerDto[] dtos) {
    int numberOfEntries = dtos.length;
    long numberOfUniqueEntries = Arrays.stream(dtos)
      .distinct()
      .count();
    return numberOfEntries != numberOfUniqueEntries;
  }

}
