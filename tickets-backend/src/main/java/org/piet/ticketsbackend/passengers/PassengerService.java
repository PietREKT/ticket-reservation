package org.piet.ticketsbackend.passengers;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

// SOLID: SRP - zarządza tylko CRUD pasażerów
@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    public PassengerEntity create(PassengerRequest request) {
        PassengerEntity passenger = passengerMapper.fromRequest(request);
        return passengerRepository.save(passenger);
    }

    public PassengerEntity getById(UUID id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Passenger not found"));
    }

    public Page<PassengerEntity> getPage(PaginationDto pagination) {
        return passengerRepository.findAll(pagination.toPageable());
    }

    public PassengerEntity update(UUID id, PassengerRequest request) {
        PassengerEntity existing = getById(id);
        passengerMapper.copyToEntity(request, existing);
        return passengerRepository.save(existing);
    }

    public void delete(UUID id) {
        PassengerEntity existing = getById(id);
        passengerRepository.delete(existing);
    }
}
