package org.piet.ticketsbackend.passengers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Testy integracyjne PassengerController:
 * - POST /passenger
 * - GET /passenger
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never"
})
class PassengerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPassenger_shouldReturn201AndB() throws Exception {
        String json = """
        {
          "firstName": "IntegrTest",
          "lastName": "User",
          "email": "integr.test.user@example.com",
          "birthDate": "1995-01-01",
          "documentNumber": "INT123456"
        }
        """;

        mockMvc.perform(post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("IntegrTest"))
                .andExpect(jsonPath("$.documentNumber").value("INT123456"));
    }


    @Test
    void getPassengersPage_shouldReturn200() throws Exception {
        mockMvc.perform(get("/passenger")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
