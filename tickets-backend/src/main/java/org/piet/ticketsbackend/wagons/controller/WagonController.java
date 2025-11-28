package org.piet.ticketsbackend.wagons.controller;

import jakarta.validation.Valid;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.wagons.dto.WagonCreateDto;
import org.piet.ticketsbackend.wagons.dto.WagonDto;
import org.piet.ticketsbackend.wagons.dto.WagonUpdateDto;
import org.piet.ticketsbackend.wagons.service.WagonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wagons")
public class WagonController {

    private final WagonService service;

    public WagonController(WagonService service) {
        this.service = service;
    }

    @GetMapping
    public PageDto<WagonDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAll(page, size);
    }

    @GetMapping("/train/{trainId}")
    public PageDto<WagonDto> getByTrain(
            @PathVariable Long trainId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getByTrain(trainId, page, size);
    }

    @GetMapping("/{id}")
    public WagonDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public WagonDto create(@Valid @RequestBody WagonCreateDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public WagonDto update(@PathVariable Long id, @Valid @RequestBody WagonUpdateDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
