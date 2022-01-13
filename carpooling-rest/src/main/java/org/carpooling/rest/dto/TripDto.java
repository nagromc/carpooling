package org.carpooling.rest.dto;

import java.util.Set;

public record TripDto(String driver, Set<String> passengers) {
}
