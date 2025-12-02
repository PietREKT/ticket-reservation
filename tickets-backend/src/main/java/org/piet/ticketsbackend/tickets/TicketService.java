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

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final SeatApiClient seatApiClient;
    private final RouteApiClient routeApiClient;

    @Transactional
    public TicketEntity buyTicket(TicketPurchaseRequest request) {

        PassengerEntity passenger = passengerRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found"));

        SeatAllocationResponse seat = seatApiClient.allocateSeat(
                request.getTrainId(),
                request.getWagonId(),
                request.getCoachNumber(),
                request.getTravelDate()
        );

        RouteInfo routeInfo = routeApiClient.getRouteInfo(
                request.getRouteId(),
                request.getTravelDate(),
                request.getStartStationCode(),
                request.getEndStationCode()
        );

        BigDecimal price = routeInfo.getBasePrice();
        TicketType type = request.getTicketType();
        if (type == TicketType.DISCOUNT) {
            price = price.multiply(new BigDecimal("0.5"));
        }

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
                .ticketType(type)
                .build();

        return ticketRepository.save(ticket);
    }

    @Transactional
    public void cancelTicket(Long id) {
        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        seatApiClient.releaseSeat(
                ticket.getTrainId(),
                ticket.getWagonId(),
                ticket.getSeatNumber(),
                ticket.getTravelDate()
        );

        ticket.setStatus(TicketStatus.CANCELED);
        ticketRepository.save(ticket);
    }

    public Page<TicketEntity> getTicketsForPassenger(UUID passengerId, PaginationDto paginationDto) {
        return ticketRepository.findByPassenger_Id(
                passengerId,
                paginationDto.toPageable()
        );
    }
}
