package org.piet.ticketsbackend.passengers;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.piet.ticketsbackend.passengers.dto.PassengerResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService service;
    private final PassengerMapper mapper;

    @PostMapping
    public PassengerResponse create(@RequestBody PassengerRequest req) {
        return mapper.toResponse(service.create(req));
    }

    @GetMapping("/{id}")
    public PassengerResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.get(id));
    }

    @GetMapping
    public List<PassengerResponse> getAll() {
        return service.getAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public PassengerResponse update(@PathVariable Long id, @RequestBody PassengerRequest req) {
        return mapper.toResponse(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
