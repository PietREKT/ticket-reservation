package org.piet.ticketsbackend.tickets.service;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.passengers.PassengerEntity;
import org.piet.ticketsbackend.passengers.PassengerRepository;
import org.piet.ticketsbackend.tickets.TicketEntity;
import org.piet.ticketsbackend.tickets.TicketRepository;
import org.piet.ticketsbackend.tickets.TicketStatus;
import org.piet.ticketsbackend.tickets.TicketType;
import org.piet.ticketsbackend.tickets.client.SeatApiClient;
import org.piet.ticketsbackend.tickets.client.SeatApiClient.SeatAllocationResponse;
import org.piet.ticketsbackend.tickets.client.RouteApiClient;
import org.piet.ticketsbackend.tickets.client.RouteApiClient.RouteInfo;
import org.piet.ticketsbackend.tickets.dto.TicketPurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

// SOLID: SRP - Klasa skupia się wyłącznie na logice biznesowej biletów
// SOLID: DIP - Zależność od abstrakcji (interfejsów klientów API)
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final SeatApiClient seatApiClient;  // SOLID: DIP - wstrzykiwanie interfejsu
    private final RouteApiClient routeApiClient; // SOLID: DIP - wstrzykiwanie interfejsu

    // SOLID: SRP - zakup biletu
    @Transactional
    public TicketEntity buyTicket(TicketPurchaseRequest request) {
        PassengerEntity passenger = passengerRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found"));

        // SOLID: SRP - delegacja alokacji siedzenia do dedykowanego klienta
        SeatAllocationResponse seat = seatApiClient.allocateSeat(
                request.getTrainId(), request.getWagonId(), request.getCoachNumber(), request.getTravelDate());

        // SOLID: SRP - delegacja informacji o trasie do dedykowanego klienta
        RouteInfo routeInfo = routeApiClient.getRouteInfo(
                request.getRouteId(), request.getTravelDate(), request.getStartStationCode(), request.getEndStationCode());

        BigDecimal price = calculatePrice(routeInfo.getBasePrice(), request.getTicketType()); // SOLID: SRP - wyodrębnienie kalkulacji ceny

        TicketEntity ticket = TicketEntity.builder()
                .passenger(passenger)
                .trainId(seat.trainId())
                .trainName(seat.trainName())
                .wagonId(seat.wagonId())
                .coachNumber(request.getCoachNumber())
                .seatNumber(seat.seatNumber())
                .routeId(request.getRouteId())
                .startStationCode(request.getStartStationCode())
                .endStationCode(request.getEndStationCode())
                .startStationName(routeInfo.getStartStationName())
                .endStationName(routeInfo.getEndStationName())
                .departureTime(routeInfo.getDepartureTime())
                .price(price)
                .travelDate(request.getTravelDate())
                .status(TicketStatus.ACTIVE)
                .ticketType(request.getTicketType())
                .build();

        return ticketRepository.save(ticket);
    }

    // SOLID: SRP - pojedyncza metoda do kalkulacji ceny
    private BigDecimal calculatePrice(BigDecimal basePrice, TicketType type) {
        if (type == TicketType.DISCOUNT) {
            return basePrice.multiply(new BigDecimal("0.5"));
        }
        return basePrice;
    }

    // SOLID: SRP - anulowanie biletu
    @Transactional
    public void cancelTicket(Long id) {
        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        // SOLID: SRP - delegacja zwalniania siedzenia
        seatApiClient.releaseSeat(ticket.getTrainId(), ticket.getWagonId(), ticket.getSeatNumber(), ticket.getTravelDate());

        ticket.setStatus(TicketStatus.CANCELED);
        ticketRepository.save(ticket);
    }

    // SOLID: SRP - wyszukiwanie biletów pasażera
    public Page<TicketEntity> getTicketsForPassenger(UUID passengerId, PaginationDto paginationDto) {
        return ticketRepository.findByPassengerId(passengerId, paginationDto.toPageable());
    }
}
