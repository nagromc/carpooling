package org.carpooling.database.sql;

import org.carpooling.database.sql.dao.CarpoolerDao;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.CarpoolerRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class SqlCarpoolerRepository implements CarpoolerRepository {
  private final CarpoolerCrudRepository crudRepository;

  public SqlCarpoolerRepository(CarpoolerCrudRepository crudRepository) {
    this.crudRepository = crudRepository;
  }

  @Override
  public void add(Carpooler carpooler) {
    var dao = new CarpoolerDao();
    dao.setId(carpooler.id());
    dao.setDisplayName(carpooler.displayName());
    crudRepository.save(dao);
  }

  @Override
  public Carpooler findById(String id) {
    var possibleDao = crudRepository.findById(id);
    if (possibleDao.isEmpty()) throw new NotFoundException(id);

    var dao = possibleDao.get();
    return convertDaoToCarpooler(dao);
  }

  @Override
  public Set<Carpooler> findAll() {
    return crudRepository.findAll().stream()
      .map(this::convertDaoToCarpooler)
      .collect(Collectors.toSet());
  }

  private Carpooler convertDaoToCarpooler(CarpoolerDao dao) {
    return new Carpooler(dao.getId(), dao.getDisplayName());
  }
}
