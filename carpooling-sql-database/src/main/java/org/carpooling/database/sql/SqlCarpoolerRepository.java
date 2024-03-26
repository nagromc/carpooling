package org.carpooling.database.sql;

import org.carpooling.database.sql.dao.CarpoolerDao;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.CarpoolerRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SqlCarpoolerRepository implements CarpoolerRepository {
  private final CrudRepository<CarpoolerDao, String> crudRepository;

  public SqlCarpoolerRepository(CrudRepository<CarpoolerDao, String> crudRepository) {
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
    return StreamSupport.stream(crudRepository.findAll().spliterator(), true)
      .map(this::convertDaoToCarpooler)
      .collect(Collectors.toSet());
  }

  private Carpooler convertDaoToCarpooler(CarpoolerDao dao) {
    return new Carpooler(dao.getId(), dao.getDisplayName());
  }
}
