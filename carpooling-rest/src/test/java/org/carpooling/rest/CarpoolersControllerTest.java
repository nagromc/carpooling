package org.carpooling.rest;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.ListCarpoolersUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarpoolersController.class)
@AutoConfigureMockMvc
public class CarpoolersControllerTest {

  public static final Carpooler ALICE = new Carpooler("alice", "Alice");
  public static final Carpooler BOB = new Carpooler("bob", "Bob");

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ListCarpoolersUseCase listCarpoolersUseCase;

  @Test
  void shouldReturnCarpoolers() throws Exception {
    Set<Carpooler> carpoolers = Set.of(ALICE, BOB);
    when(listCarpoolersUseCase.execute()).thenReturn(carpoolers);

    mockMvc.perform(get("/carpoolers"))
      .andExpect(status().isOk())
      .andExpect(content().json("""
        [
          {"id":"alice", "name": "Alice"},
          {"id":"bob", "name": "Bob"}
        ]"""));
  }

}
