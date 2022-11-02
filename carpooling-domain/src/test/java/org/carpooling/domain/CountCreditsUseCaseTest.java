package org.carpooling.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountCreditsUseCaseTest {

  @Test
  void givenCreditsSumIsNotZero_shouldThrowException() {
    Map<Carpooler, Float> inconsistentCredits = new HashMap<>();
    inconsistentCredits.put(new Carpooler("alice"), 1.23f);
    inconsistentCredits.put(new Carpooler("bob"), 0f);
    CountCreditsUseCase useCase = new CountCreditsUseCase(new InMemoryTripRepository(), inconsistentCredits);

    InconsistentCalculatedCreditsException exception = assertThrows(InconsistentCalculatedCreditsException.class, useCase::execute);
    assertThat(exception.getMessage()).isEqualTo("The sum of all credits should be 0, but was [1.230000]");
    assertThat(exception.getCalculatedSum()).isCloseTo(1.23f, withinPercentage(0.01));
  }

}
