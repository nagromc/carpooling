package org.carpooling.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListCarpoolersUseCaseTest {

  public static final Carpooler ALICE = new Carpooler("alice", "Alice");
  public static final Carpooler BOB = new Carpooler("bob", "Bob");
  private ListCarpoolersUseCase useCase;
  private InMemoryCarpoolerRepository carpoolerRepository;

  @BeforeEach
  void setUp() {
    carpoolerRepository = new InMemoryCarpoolerRepository();
    useCase = new ListCarpoolersUseCase(carpoolerRepository);
  }

  @Test
  void givenNoCarpooler_shouldReturnEmptySet() {
    var result = useCase.execute();

    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  void givenCarpoolers_shouldReturnNonEmptySet() {
    carpoolerRepository.add(ALICE);
    carpoolerRepository.add(BOB);

    var result = useCase.execute();

    assertThat(result)
      .isNotNull()
      .hasSize(2)
      .containsExactlyInAnyOrder(ALICE, BOB);
  }

}
