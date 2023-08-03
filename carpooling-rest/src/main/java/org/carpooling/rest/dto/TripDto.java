package org.carpooling.rest.dto;

import java.time.LocalDate;
import java.util.Set;

public record TripDto(LocalDate date, Set<String> drivers, Set<String> passengers) {
}
