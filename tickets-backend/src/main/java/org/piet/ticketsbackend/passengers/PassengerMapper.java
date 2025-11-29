package org.piet.ticketsbackend.passengers;

import org.piet.ticketsbackend.passengers.dto.PassengerResponse;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {

    public PassengerResponse toResponse(PassengerEntity p) {
        PassengerResponse dto = new PassengerResponse();

        dto.setId(p.getId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setEmail(p.getEmail());
        dto.setBirthDate(p.getBirthDate());
        dto.setDocumentNumber(p.getDocumentNumber());

        if (p.getUser() != null) {
            dto.setUserId(p.getUser().getId());
        }

        return dto;
    }
}
