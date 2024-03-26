package org.carpooling.database.sql;

import org.carpooling.database.sql.dao.CarpoolerDao;
import org.springframework.data.repository.CrudRepository;

public interface CarpoolerCrudRepository extends CrudRepository<CarpoolerDao, String> {
}
