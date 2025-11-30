package org.piet.ticketsbackend.passengers;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PasswordEncoder passwordEncoder;
    private final PassengerMapper passengerMapper;

    public PassengerEntity create(PassengerRequest request) {
        PassengerEntity passenger = passengerMapper.fromRequest(request);
        passenger.setPassword(passwordEncoder.encode(request.getPassword()));
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

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return passengerRepository.save(existing);
    }

    public void delete(UUID id) {
        PassengerEntity existing = getById(id);
        passengerRepository.delete(existing);
    }
}
