package org.carpooling.rest.dto;

import java.time.LocalDate;
import java.util.Set;

public record TripDto(LocalDate date, String driver, Set<String> passengers) {
}
