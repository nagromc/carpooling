package org.carpooling.rest;

import org.carpooling.domain.Carpooler;
import org.carpooling.domain.CountCreditsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditsController.class)
@AutoConfigureMockMvc
public class CreditsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CountCreditsUseCase countCreditsUseCase;

  @Test
  void shouldReturnCredits() throws Exception {
    Map<Carpooler, Float> credits = new HashMap<>();
    credits.put(new Carpooler("alice"), 0.1337f);
    credits.put(new Carpooler("bob"), -0.42f);
    when(countCreditsUseCase.execute()).thenReturn(credits);

    mockMvc.perform(get("/credits"))
      .andExpect(status().isOk())
      .andExpect(content().json("""
        {
          "alice": 0.1337,
          "bob": -0.42
        }"""));
  }

}
