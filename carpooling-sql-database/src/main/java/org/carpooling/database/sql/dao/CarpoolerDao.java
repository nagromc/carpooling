package org.carpooling.database.sql.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CarpoolerDao {
  @Id
  private String id;
  private String displayName;

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
}
