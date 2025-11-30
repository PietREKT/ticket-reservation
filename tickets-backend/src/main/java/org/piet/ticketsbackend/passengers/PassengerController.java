package org.piet.ticketsbackend.passengers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.piet.ticketsbackend.passengers.dto.PassengerResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @PostMapping
    public PassengerResponse create(@Valid @RequestBody PassengerRequest request) {
        return passengerMapper.toResponse(passengerService.create(request));
    }

    @GetMapping("/{id}")
    public PassengerResponse getById(@PathVariable UUID id) {
        return passengerMapper.toResponse(passengerService.getById(id));
    }

    @GetMapping
    public PageDto<PassengerResponse> getPage(PaginationDto paginationDto) {
        Page<PassengerEntity> page = passengerService.getPage(paginationDto);
        Page<PassengerResponse> mapped = page.map(passengerMapper::toResponse);
        return PageDto.create(mapped);
    }

    @PutMapping("/{id}")
    public PassengerResponse update(@PathVariable UUID id,
                                    @Valid @RequestBody PassengerRequest request) {
        return passengerMapper.toResponse(passengerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        passengerService.delete(id);
    }
}
