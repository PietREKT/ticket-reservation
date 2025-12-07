package org.piet.ticketsbackend.TrainsAndWagons;

import org.junit.jupiter.api.Test;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.trains.dto.TrainCreateDto;
import org.piet.ticketsbackend.trains.dto.TrainDto;
import org.piet.ticketsbackend.trains.service.TrainService;
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
class TrainControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainService trainService;

    @Test
    void shouldReturnTrainsPage() throws Exception {
        TrainDto t = new TrainDto();
        t.setId(1L);
        t.setModel("IC");
        t.setNumber("TLK1234");
        t.setLineNumber("L1");
        t.setSpeed(120);
        t.setWagonCount(5);
        t.setRouteId(10L);

        Page<TrainDto> page = new PageImpl<>(List.of(t), PageRequest.of(0, 10), 1);
        PageDto<TrainDto> pageDto = PageDto.create(page);

        when(trainService.getAll(any())).thenReturn(pageDto);

        mockMvc.perform(get("/api/trains")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].number").value("TLK1234"));
    }

    @Test
    void shouldCreateTrain() throws Exception {
        String body = """
            {
              "model": "IC",
              "number": "TLK1234",
              "lineNumber": "L1",
              "speed": 120,
              "wagonCount": 5,
              "routeId": 10
            }
            """;

        TrainDto created = new TrainDto();
        created.setId(1L);
        created.setModel("IC");
        created.setNumber("TLK1234");

        when(trainService.create(any(TrainCreateDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/trains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("TLK1234"));
    }
}
