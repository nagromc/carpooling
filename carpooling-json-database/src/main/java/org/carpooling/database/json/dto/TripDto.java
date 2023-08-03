package org.carpooling.database.json.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public final class TripDto {

  public final String date;
  @SerializedName("drivers")
  public final Set<String> driversId;
  @SerializedName("passengers")
  public final Set<String> passengersId;

  public TripDto(String date, Set<String> driversId, Set<String> passengersId) {
    this.date = date;
    this.driversId = driversId;
    this.passengersId = passengersId;
  }

}
