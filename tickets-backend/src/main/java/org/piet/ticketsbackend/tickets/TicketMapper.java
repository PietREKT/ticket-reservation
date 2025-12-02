package org.piet.ticketsbackend.tickets;

import org.piet.ticketsbackend.tickets.dto.MyTicketView;
import org.piet.ticketsbackend.tickets.dto.TicketResponse;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponse toResponse(TicketEntity t) {
        TicketResponse dto = new TicketResponse();

        dto.setId(t.getId());
        dto.setPassengerId(t.getPassenger().getId());
        dto.setPassengerName(
                t.getPassenger().getFirstName() + " " + t.getPassenger().getLastName()
        );

        dto.setTrainId(t.getTrainId());
        dto.setTrainName(t.getTrainName());

        // info o wagonie
        dto.setWagonId(t.getWagonId());
        dto.setCoachNumber(t.getCoachNumber());
        dto.setSeatNumber(t.getSeatNumber());

        dto.setRouteId(t.getRouteId());
        dto.setStartStationCode(t.getStartStationCode());
        dto.setEndStationCode(t.getEndStationCode());
        dto.setStartStationName(t.getStartStationName());
        dto.setEndStationName(t.getEndStationName());
        dto.setDepartureTime(t.getDepartureTime());
        dto.setTravelDate(t.getTravelDate());
        dto.setPrice(t.getPrice());
        dto.setTicketType(t.getTicketType());
        dto.setStatus(t.getStatus());

        return dto;
    }

    public MyTicketView toMyTicketView(TicketEntity t) {
        MyTicketView v = new MyTicketView();

        v.setTicketId(t.getId());
        v.setTrainName(t.getTrainName());
        v.setCoachNumber(t.getCoachNumber());
        v.setSeatNumber(t.getSeatNumber());
        v.setStartStationName(t.getStartStationName());
        v.setEndStationName(t.getEndStationName());
        v.setDepartureTime(t.getDepartureTime());
        v.setPrice(t.getPrice());
        v.setTicketType(t.getTicketType());
        v.setStatus(t.getStatus());

        return v;
    }
}
