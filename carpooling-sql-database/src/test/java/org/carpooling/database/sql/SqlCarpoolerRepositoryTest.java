package org.carpooling.database.sql;

import org.carpooling.database.sql.dao.CarpoolerDao;
import org.carpooling.domain.Carpooler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SqlCarpoolerRepositoryTest {

  private SqlCarpoolerRepository repository;
  private MockCarpoolerCrudRepository mockCrudRepository;

  @BeforeEach
  void setUp() {
    mockCrudRepository = new MockCarpoolerCrudRepository();
    repository = new SqlCarpoolerRepository(mockCrudRepository);
  }

  @Test
  void shouldAddCarpooler() {
    mockCrudRepository.setExpectedSavedCarpooler(new CarpoolerDao("foo", "bar"));

    repository.add(new Carpooler("foo", "bar"));

    mockCrudRepository.verifySave();
  }

  @Test
  void shouldReturnAllCarpoolers() {
    var carpoolers = repository.findAll();

    assertThat(carpoolers)
      .singleElement()
      .isEqualTo(new Carpooler("foo", "bar"));
    mockCrudRepository.verifyFindAllHasBeenCall();
  }

  @Test
  void shouldReturnTheCarpooler() {
    mockCrudRepository.setExpectedId("foo");

    var carpooler = repository.findById("foo");

    assertThat(carpooler).isNotNull();
    mockCrudRepository.verifyFindById();
  }
}
