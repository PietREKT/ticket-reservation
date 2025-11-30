package org.piet.ticketsbackend.wagons.controller;

import jakarta.validation.Valid;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.wagons.dto.WagonCreateDto;
import org.piet.ticketsbackend.wagons.dto.WagonDto;
import org.piet.ticketsbackend.wagons.dto.WagonUpdateDto;
import org.piet.ticketsbackend.wagons.service.WagonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.prefix}/wagons")
public class WagonController {

    private final WagonService service;

    public WagonController(WagonService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PageDto<WagonDto>> getAll(PaginationDto paginationDto) {
        return ResponseEntity.ok(service.getAll(paginationDto));
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<PageDto<WagonDto>> getByTrain(
            @PathVariable Long trainId,
            PaginationDto paginationDto
    ) {
        return ResponseEntity.ok(service.getByTrain(trainId, paginationDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WagonDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<WagonDto> create(@Valid @RequestBody WagonCreateDto dto) {
        WagonDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WagonDto> update(
            @PathVariable Long id,
            @Valid @RequestBody WagonUpdateDto dto
    ) {
        WagonDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
