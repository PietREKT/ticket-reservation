package org.piet.ticketsbackend.tickets.service;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.passengers.PassengerEntity;
import org.piet.ticketsbackend.passengers.PassengerRepository;
import org.piet.ticketsbackend.tickets.TicketEntity;
import org.piet.ticketsbackend.tickets.TicketRepository;
import org.piet.ticketsbackend.tickets.TicketStatus;
import org.piet.ticketsbackend.tickets.client.SeatApiClient;
import org.piet.ticketsbackend.tickets.client.SeatApiClient.SeatAllocationResponse;
import org.piet.ticketsbackend.tickets.dto.TicketPurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final SeatApiClient seatApiClient;

    @Transactional
    public TicketEntity buyTicket(TicketPurchaseRequest request) {

        PassengerEntity passenger = passengerRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found"));

        // SeatApiClient sam waliduje wagon + pociąg
        SeatAllocationResponse seat = seatApiClient.allocateSeat(
                request.getTrainId(),
                request.getWagonId(),
                request.getCoachNumber(),
                request.getTravelDate()
        );

        // tworzymy bilet tylko z tymi danymi, które faktycznie mamy
        TicketEntity ticket = TicketEntity.builder()
                .passenger(passenger)
                .trainId(seat.trainId())
                .wagonId(seat.wagonId())
                .coachNumber(request.getCoachNumber())   // numer wagonu w składzie
                .seatNumber(seat.seatNumber())           // przydzielone miejsce w wagonie
                .routeId(request.getRouteId())
                .startStationCode(request.getStartStationCode())
                .endStationCode(request.getEndStationCode())
                .travelDate(request.getTravelDate())
                .status(TicketStatus.ACTIVE)
                .ticketType(request.getTicketType())
                .build();


        return ticketRepository.save(ticket);
    }

    @Transactional
    public void cancelTicket(Long id) {
        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        // releaseSeat ma taką samą sygnaturę jak allocateSeat
        seatApiClient.releaseSeat(
                ticket.getTrainId(),
                ticket.getWagonId(),
                ticket.getCoachNumber(),
                ticket.getTravelDate()
        );

        ticket.setStatus(TicketStatus.CANCELED);
        ticketRepository.save(ticket);
    }

    public Page<?> getTicketsForPassenger(UUID passengerId, PaginationDto paginationDto) {
        return ticketRepository.findByPassenger_Id(
                passengerId,
                paginationDto.toPageable()
        );
    }
}
