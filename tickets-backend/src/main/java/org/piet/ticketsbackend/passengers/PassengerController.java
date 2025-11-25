package org.piet.ticketsbackend.passengers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.piet.ticketsbackend.passengers.dto.PassengerResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    public PassengerResponse create(@Valid @RequestBody PassengerRequest request) {
        return passengerService.create(request);
    }

    @GetMapping("/{id}")
    public PassengerResponse get(@PathVariable Long id) {
        return passengerService.get(id);
    }

    @PutMapping("/{id}")
    public PassengerResponse update(@PathVariable Long id, @Valid @RequestBody PassengerRequest request) {
        return passengerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        passengerService.delete(id);
    }

    @GetMapping
    public List<PassengerResponse> getAll() {
        return passengerService.getAll();
    }
}
