package org.piet.ticketsbackend.passengers;

import org.piet.ticketsbackend.passengers.dto.PassengerRequest;
import org.piet.ticketsbackend.passengers.dto.PassengerResponse;
import org.springframework.stereotype.Component;

// SOLID: SRP - mapper przenosi dane między DTO a encją
@Component
public class PassengerMapper {

    public PassengerResponse toResponse(PassengerEntity entity) {
        PassengerResponse dto = new PassengerResponse();
        dto.setId(entity.getId());
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
        return entity;
    }

    public void copyToEntity(PassengerRequest request, PassengerEntity entity) {
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setBirthDate(request.getBirthDate());
        entity.setDocumentNumber(request.getDocumentNumber());
    }
}
