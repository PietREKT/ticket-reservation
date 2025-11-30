package org.piet.ticketsbackend.passengers;

import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.piet.ticketsbackend.passengers.dto.PassengerResponse;
import org.piet.ticketsbackend.users.entities.Role;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {

    public PassengerResponse toResponse(PassengerEntity entity) {
        PassengerResponse dto = new PassengerResponse();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setBirthDate(entity.getBirthDate());
        dto.setDocumentNumber(entity.getDocumentNumber());
        return dto;
    }

    public PassengerEntity fromRequest(PassengerRequest request) {
        PassengerEntity entity = new PassengerEntity();
        copyToEntity(request, entity);
        entity.setRole(Role.PASSENGER);
        entity.setPasswordNeedsChanging(false);
        return entity;
    }

    public void copyToEntity(PassengerRequest request, PassengerEntity entity) {
        // pola z User
        if (request.getUsername() != null) {
            entity.setUsername(request.getUsername());
        }

        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setBirthDate(request.getBirthDate());
        entity.setDocumentNumber(request.getDocumentNumber());
    }
}
