package org.carpooling.rest;

import org.carpooling.domain.CarPoolUseCase;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.ListTripsUseCase;
import org.carpooling.domain.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TripControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ListTripsUseCase listTripsUseCase;

  @MockBean
  private CarPoolUseCase carPoolUseCase;

  public static final Carpooler ALICE = new Carpooler("Alice");
  public static final Carpooler BOB = new Carpooler("Bob");
  public static final Carpooler CHARLIE = new Carpooler("Charlie");

  @Test
  void shouldReturnAllTrips() throws Exception {
    List<Trip> initialTrips = List.of(
      new Trip(ALICE, Set.of(BOB, CHARLIE)),
      new Trip(BOB, Set.of(ALICE))
    );
    when(listTripsUseCase.execute()).thenReturn(initialTrips);

    mockMvc.perform(get("/trip/all"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        """
        [
          {"driver":"Alice",
            "passengers":["Bob", "Charlie"]},
          {"driver":"Bob",
            "passengers":["Alice"]}
        ]"""
      ));
  }

  @Test
  void shouldAddATrip() throws Exception {
    mockMvc.perform(
        post("/trip")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "driver": "Charlie",
              "passengers": ["Bob"]
            }
            """)
      )
      .andExpect(status().isOk());

    verify(carPoolUseCase).execute(
      CHARLIE,
      Set.of(BOB)
    );
  }

}
