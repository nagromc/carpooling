package org.carpooling.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE;

class CountCreditsUseCaseTest {

  @Test
  void givenCreditsSumIsNotZero_shouldThrowException() {
    Map<Carpooler, Float> inconsistentCredits = new HashMap<>();
    inconsistentCredits.put(new Carpooler("alice"), 1.23f);
    inconsistentCredits.put(new Carpooler("bob"), 0f);
    var useCase = new CountCreditsUseCase(new InMemoryTripRepository(), inconsistentCredits);

    assertThatExceptionOfType(InconsistentCalculatedCreditsException.class)
      .isThrownBy(useCase::execute)
      .withMessage("The sum of all credits should be 0, but was [1.230000]")
      .extracting(InconsistentCalculatedCreditsException::getCalculatedSum, DOUBLE)
      .isCloseTo(1.23f, withinPercentage(0.01));
  }

}
