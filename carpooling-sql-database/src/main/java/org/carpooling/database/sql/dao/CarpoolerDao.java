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
