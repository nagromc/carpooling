package org.carpooling.database.json.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public final class TripDto {

  @SerializedName("driver")
  public final String driverId;
  @SerializedName("passengers")
  public final Set<String> passengersId;

  public TripDto(String driverId, Set<String> passengersId) {
    this.driverId = driverId;
    this.passengersId = passengersId;
  }

}
