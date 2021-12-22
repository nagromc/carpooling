package org.carpooling.database.json.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public final class TripDto {

  @SerializedName("driver")
  public final String driverName;
  @SerializedName("passengers")
  public final Set<String> passengersName;

  public TripDto(String driverName, Set<String> passengersName) {
    this.driverName = driverName;
    this.passengersName = passengersName;
  }

}
