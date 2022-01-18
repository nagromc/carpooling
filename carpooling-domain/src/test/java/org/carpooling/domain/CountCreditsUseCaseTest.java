package org.carpooling.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountCreditsUseCaseTest {

  @Test
  void givenCreditsSumIsNotZero_shouldThrowException() {
    Map<Carpooler, Float> inconsistentCredits = new HashMap<>();
    inconsistentCredits.put(new Carpooler("alice"), 1.23f);
    inconsistentCredits.put(new Carpooler("bob"), 0f);
    CountCreditsUseCase useCase = new CountCreditsUseCase(new InMemoryTripRepository(), inconsistentCredits);

    InconsistentCalculatedCreditsException exception = assertThrows(InconsistentCalculatedCreditsException.class, useCase::execute);
    assertThat(exception.getMessage(), is("The sum of all credits should be 0, but was [1.230000]"));
    assertThat(exception.getCalculatedSum(), closeTo(1.23f, 0.01));
  }

}
