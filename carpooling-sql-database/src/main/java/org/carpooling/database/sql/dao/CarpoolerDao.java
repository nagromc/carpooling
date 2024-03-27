package org.carpooling.database.sql.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "carpooler")
public class CarpoolerDao {
  @Id
  @Column(length = 20)
  private String id;
  @Column(length = 30)
  private String displayName;

  public CarpoolerDao() {
  }

  public CarpoolerDao(String id, String displayName) {
    this.id = id;
    this.displayName = displayName;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CarpoolerDao that = (CarpoolerDao) o;
    return id.equals(that.id) && displayName.equals(that.displayName);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + displayName.hashCode();
    return result;
  }
}
