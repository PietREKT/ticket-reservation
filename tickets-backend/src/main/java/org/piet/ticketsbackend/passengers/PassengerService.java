package org.piet.ticketsbackend.passengers;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.piet.ticketsbackend.users.entities.User;
import org.piet.ticketsbackend.users.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;

    public PassengerEntity create(PassengerRequest req) {
        PassengerEntity entity = new PassengerEntity();

        entity.setFirstName(req.getFirstName());
        entity.setLastName(req.getLastName());
        entity.setEmail(req.getEmail());
        entity.setBirthDate(req.getBirthDate());
        entity.setDocumentNumber(req.getDocumentNumber());

        if (req.getUserId() != null) {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            entity.setUser(user);
        }

        return passengerRepository.save(entity);
    }

    public PassengerEntity get(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found"));
    }

    public List<PassengerEntity> getAll() {
        return passengerRepository.findAll();
    }

    public PassengerEntity update(Long id, PassengerRequest req) {
        PassengerEntity entity = get(id);

        entity.setFirstName(req.getFirstName());
        entity.setLastName(req.getLastName());
        entity.setEmail(req.getEmail());
        entity.setBirthDate(req.getBirthDate());
        entity.setDocumentNumber(req.getDocumentNumber());

        if (req.getUserId() != null) {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            entity.setUser(user);
        }

        return passengerRepository.save(entity);
    }

    public void delete(Long id) {
        passengerRepository.deleteById(id);
    }
}
