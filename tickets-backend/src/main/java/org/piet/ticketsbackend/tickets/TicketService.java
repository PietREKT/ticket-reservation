package org.piet.ticketsbackend.tickets;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.passengers.PassengerEntity;
import org.piet.ticketsbackend.passengers.PassengerRepository;
import org.piet.ticketsbackend.tickets.client.RouteApiClient;
import org.piet.ticketsbackend.tickets.client.SeatApiClient;
import org.piet.ticketsbackend.tickets.dto.TicketPurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final RouteApiClient routeApiClient;
    private final SeatApiClient seatApiClient;

    public TicketEntity buyTicket(TicketPurchaseRequest request) {

        PassengerEntity passenger = passengerRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found"));

        var routeInfo = routeApiClient.getRouteInfo(
                request.getRouteId(),
                request.getTravelDate(),
                request.getStartStationCode(),
                request.getEndStationCode()
        );

        var seat = seatApiClient.allocateSeat(
                request.getTrainId(),
                request.getWagonId(),
                request.getCoachNumber(),
                request.getTravelDate()
        );

        if (seat == null || !seat.isSuccess()) {
            throw new BadRequestException("No free seats for selected train/wagon");
        }

        BigDecimal price = routeInfo.getBasePrice();
        if (request.getTicketType() == TicketType.DISCOUNT) {
            price = price.multiply(new BigDecimal("0.5")); // 50% ulgi
        }
        price = price.setScale(2, RoundingMode.HALF_UP);

        TicketEntity ticket = TicketEntity.builder()
                .passenger(passenger)
                .trainId(seat.getTrainId())
                .wagonId(seat.getWagonId())
                .coachNumber(seat.getCoachNumber())
                .seatNumber(seat.getSeatNumber())
                .trainName(seat.getTrainName())

                .routeId(routeInfo.getRouteId())
                .startStationCode(request.getStartStationCode())
                .endStationCode(request.getEndStationCode())
                .startStationName(routeInfo.getStartStationName())
                .endStationName(routeInfo.getEndStationName())
                .departureTime(routeInfo.getDepartureTime())
                .travelDate(request.getTravelDate())

                .price(price)
                .ticketType(request.getTicketType())
                .status(TicketStatus.ACTIVE)
                .build();

        return ticketRepository.save(ticket);
    }

    public void cancelTicket(Long ticketId) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (ticket.getStatus() == TicketStatus.CANCELED) {
            return;
        }

        ticket.setStatus(TicketStatus.CANCELED);
        ticketRepository.save(ticket);

        seatApiClient.releaseSeat(
                ticket.getTrainId(),
                ticket.getWagonId(),
                ticket.getCoachNumber(),
                ticket.getSeatNumber(),
                ticket.getTravelDate()
        );
    }

    public Page<TicketEntity> getTicketsForPassenger(UUID passengerId, PaginationDto paginationDto) {
        return ticketRepository.findByPassenger_Id(
                passengerId,
                paginationDto.toPageable()
        );
    }
}
