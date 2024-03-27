package org.carpooling.database.sql;

import org.carpooling.database.sql.dao.CarpoolerDao;
import org.springframework.data.repository.ListCrudRepository;

public interface CarpoolerCrudRepository extends ListCrudRepository<CarpoolerDao, String> {
}
