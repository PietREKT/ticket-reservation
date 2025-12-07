package org.piet.ticketsbackend.tickets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void buyTicket_shouldReturn404WhenPassengerNotFound() throws Exception {
        String json = """
        {
          "passengerId": "11111111-1111-1111-1111-111111111111",
          "trainId": 10,
          "wagonId": 5,
          "coachNumber": 1,
          "routeId": 1,
          "startStationCode": "WAW",
          "endStationCode": "KRK",
          "travelDate": "2025-12-01",
          "ticketType": "NORMAL"
        }
        """;

        mockMvc.perform(post("/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Passenger not found"));
    }


    @Test
    void getMyTickets_shouldReturn200() throws Exception {
        mockMvc.perform(get("/ticket/my-tickets")
                        .param("passengerId", "11111111-1111-1111-1111-111111111111")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
