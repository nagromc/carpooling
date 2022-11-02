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

import java.time.LocalDate;
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

  private static final LocalDate DAY1 = LocalDate.parse("2015-10-21");
  private static final LocalDate DAY2 = LocalDate.parse("2015-10-22");

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
      new Trip(DAY1, ALICE, Set.of(BOB, CHARLIE)),
      new Trip(DAY2, BOB, Set.of(ALICE))
    );
    when(listTripsUseCase.execute()).thenReturn(initialTrips);

    mockMvc.perform(get("/trips"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        """
        [
          {
            "date": "2015-10-21",
            "driver":"alice",
            "passengers":["bob", "charlie"]
          },
          {
            "date": "2015-10-22",
            "driver":"bob",
            "passengers":["alice"]
          }
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
              "date": "2015-10-21",
              "driver": "charlie",
              "passengers": ["bob"]
            }
            """)
      )
      .andExpect(status().isOk());

    verify(carPoolUseCase).execute(
      LocalDate.parse("2015-10-21"),
      CHARLIE,
      Set.of(BOB)
    );
  }

}
