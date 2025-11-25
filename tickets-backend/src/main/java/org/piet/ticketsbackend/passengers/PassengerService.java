package org.piet.ticketsbackend.passengers;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.piet.ticketsbackend.passengers.dto.PassengerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerResponse create(PassengerRequest request) {
        PassengerEntity passenger = PassengerEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .documentNumber(request.getDocumentNumber())
                .birthDate(request.getBirthDate())
                .build();

        return mapToResponse(passengerRepository.save(passenger));
    }

    public PassengerResponse get(Long id) {
        PassengerEntity passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found"));
        return mapToResponse(passenger);
    }

    public PassengerResponse update(Long id, PassengerRequest request) {
        PassengerEntity passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found"));

        passenger.setFirstName(request.getFirstName());
        passenger.setLastName(request.getLastName());
        passenger.setEmail(request.getEmail());
        passenger.setDocumentNumber(request.getDocumentNumber());
        passenger.setBirthDate(request.getBirthDate());

        return mapToResponse(passengerRepository.save(passenger));
    }

    public void delete(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found");
        }
        passengerRepository.deleteById(id);
    }

    public List<PassengerResponse> getAll() {
        return passengerRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    private PassengerResponse mapToResponse(PassengerEntity p) {
        return PassengerResponse.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .documentNumber(p.getDocumentNumber())
                .birthDate(p.getBirthDate())
                .build();
    }
}
