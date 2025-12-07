package org.piet.ticketsbackend.TrainsAndWagons;

import org.junit.jupiter.api.Test;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.wagons.dto.WagonCreateDto;
import org.piet.ticketsbackend.wagons.dto.WagonDto;
import org.piet.ticketsbackend.wagons.service.WagonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never",
        "spring.config.import=optional:file:.env[.properties]",
        "app.api.prefix=/api"
})
class WagonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WagonService wagonService;

    @Test
    void shouldReturnWagons() throws Exception {
        WagonDto w = new WagonDto();
        w.setId(1L);
        w.setNumber("W1");
        w.setSeatsTotal(80);
        w.setSeatsFree(80);
        w.setSeatClass("2");
        w.setTrainId(10L);

        Page<WagonDto> page = new PageImpl<>(List.of(w), PageRequest.of(0, 10), 1);
        PageDto<WagonDto> pageDto = PageDto.create(page);

        when(wagonService.getAll(any())).thenReturn(pageDto);

        mockMvc.perform(get("/api/wagons")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].number").value("W1"));
    }

    @Test
    void shouldCreateWagon() throws Exception {
        String body = """
            {
              "number": "W1",
              "seatsTotal": 80,
              "seatsFree": 80,
              "seatClass": "2",
              "trainId": 10
            }
            """;

        WagonDto created = new WagonDto();
        created.setId(1L);
        created.setNumber("W1");

        when(wagonService.create(any(WagonCreateDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/wagons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("W1"));
    }
}
