package org.carpooling.database.sql;

import org.carpooling.database.sql.dao.CarpoolerDao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NullableProblems")
public class MockCarpoolerCrudRepository implements CarpoolerCrudRepository {
  private CarpoolerDao expectedSavedCarpooler;
  private CarpoolerDao actualSavedCarpooler;
  private boolean findAllHasBeenCalled;
  private String expectedId;
  private String actualId;

  @Override
  public <S extends CarpoolerDao> S save(S carpooler) {
    actualSavedCarpooler = carpooler;
    return carpooler;
  }

  public void setExpectedSavedCarpooler(CarpoolerDao expectedSavedCarpooler) {
    this.expectedSavedCarpooler = expectedSavedCarpooler;
  }

  public void verifySave() {
    assertThat(actualSavedCarpooler).isEqualTo(expectedSavedCarpooler);
  }

  @Override
  public <S extends CarpoolerDao> List<S> saveAll(Iterable<S> entities) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Optional<CarpoolerDao> findById(String s) {
    actualId = s;
    return Optional.of(new CarpoolerDao("foo", "bar"));
  }

  public void setExpectedId(String id) {
    expectedId = id;
  }

  public void verifyFindById() {
    assertThat(actualId)
      .withFailMessage("Expected findById method called with [%s], but received [%s]".formatted(expectedId, actualId))
      .isEqualTo(expectedId);
  }

  @Override
  public boolean existsById(String s) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<CarpoolerDao> findAll() {
    findAllHasBeenCalled = true;
    return List.of(new CarpoolerDao("foo", "bar"));
  }

  public void verifyFindAllHasBeenCall() {
    assertThat(findAllHasBeenCalled).isTrue();
  }

  @Override
  public List<CarpoolerDao> findAllById(Iterable<String> strings) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public long count() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteById(String s) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void delete(CarpoolerDao entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteAllById(Iterable<? extends String> strings) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteAll(Iterable<? extends CarpoolerDao> entities) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
