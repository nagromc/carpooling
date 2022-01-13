package org.carpooling.rest;

import org.carpooling.domain.CarPoolUseCase;
import org.carpooling.domain.Carpooler;
import org.carpooling.domain.ListTripsUseCase;
import org.carpooling.domain.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(TripsController.class)
@AutoConfigureMockMvc
public class TripsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ListTripsUseCase listTripsUseCase;

  @MockBean
  private CarPoolUseCase carPoolUseCase;

  public static final Carpooler ALICE = new Carpooler("alice");
  public static final Carpooler BOB = new Carpooler("bob");
  public static final Carpooler CHARLIE = new Carpooler("charlie");

  @Test
  void shouldReturnAllTrips() throws Exception {
    List<Trip> initialTrips = List.of(
      new Trip(ALICE, Set.of(BOB, CHARLIE)),
      new Trip(BOB, Set.of(ALICE))
    );
    when(listTripsUseCase.execute()).thenReturn(initialTrips);

    mockMvc.perform(get("/trips"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        """
        [
          {"driver":"alice",
            "passengers":["bob", "charlie"]},
          {"driver":"bob",
            "passengers":["alice"]}
        ]"""
      ));
  }

  @Test
  void shouldAddATrip() throws Exception {
    mockMvc.perform(
        post("/trips")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "driver": "charlie",
              "passengers": ["bob"]
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
