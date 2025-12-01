package org.piet.ticketsbackend.trains.controller;

import jakarta.validation.Valid;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.trains.dto.TrainCreateDto;
import org.piet.ticketsbackend.trains.dto.TrainDto;
import org.piet.ticketsbackend.trains.dto.TrainUpdateDto;
import org.piet.ticketsbackend.trains.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.prefix}/trains")
public class TrainController {

    private final TrainService service;

    public TrainController(TrainService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PageDto<TrainDto>> getTrains(PaginationDto paginationDto) {
        PageDto<TrainDto> page = service.getAll(paginationDto);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainDto> getTrain(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<TrainDto> createTrain(@Valid @RequestBody TrainCreateDto dto) {
        TrainDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainDto> updateTrain(
            @PathVariable Long id,
            @Valid @RequestBody TrainUpdateDto dto
    ) {
        TrainDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
