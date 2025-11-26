package org.piet.ticketsbackend.tickets;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.passengers.PassengerEntity;
import org.piet.ticketsbackend.passengers.PassengerRepository;
import org.piet.ticketsbackend.tickets.client.RouteApiClient;
import org.piet.ticketsbackend.tickets.client.SeatApiClient;
import org.piet.ticketsbackend.tickets.dto.MyTicketView;
import org.piet.ticketsbackend.tickets.dto.TicketPurchaseRequest;
import org.piet.ticketsbackend.tickets.dto.TicketResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final RouteApiClient routeApiClient;
    private final SeatApiClient seatApiClient;

    public TicketResponse buyTicket(TicketPurchaseRequest req) {
        PassengerEntity passenger = passengerRepository.findById(req.getPassengerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found"));

        var routeInfo = routeApiClient.getRouteInfo(
                req.getRouteId(), req.getTravelDate(),
                req.getStartStationCode(), req.getEndStationCode()
        );

        var price = req.getTicketType() == TicketType.DISCOUNT
                ? routeInfo.getBasePrice().multiply(BigDecimal.valueOf(0.5))
                : routeInfo.getBasePrice();

        var seat = seatApiClient.allocateSeat(req.getTrainId(), req.getWagonId(), req.getTravelDate());
        if (!seat.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No free seats");
        }

        TicketEntity ticket = TicketEntity.builder()
                .passenger(passenger)
                .trainId(req.getTrainId())
                .wagonId(req.getWagonId())
                .startStation(routeInfo.getStartStationName())
                .endStation(routeInfo.getEndStationName())
                .ticketType(req.getTicketType())
                .coachNumber(seat.getCoachNumber())
                .seatNumber(seat.getSeatNumber())
                .trainName(seat.getTrainName())
                .departureTime(routeInfo.getDepartureTime())
                .travelDate(req.getTravelDate())
                .price(price)
                .status(TicketStatus.ACTIVE)
                .build();

        return mapToResponse(ticketRepository.save(ticket));
    }

    public void cancelTicket(Long id) {
        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        if (ticket.getStatus() == TicketStatus.CANCELED)
            return;

        ticket.setStatus(TicketStatus.CANCELED);
        ticketRepository.save(ticket);

        seatApiClient.releaseSeat(ticket.getTrainId(), ticket.getWagonId(),
                ticket.getSeatNumber(), ticket.getTravelDate());
    }

    public List<MyTicketView> getTicketsForPassenger(Long passengerId) {
        return ticketRepository.findByPassengerId(passengerId).stream()
                .map(this::mapToMyTicket)
                .toList();
    }

    private TicketResponse mapToResponse(TicketEntity ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .passengerId(ticket.getPassenger().getId())
                .passengerName(ticket.getPassenger().getFirstName() + " " +
                        ticket.getPassenger().getLastName())
                .trainId(ticket.getTrainId())
                .trainName(ticket.getTrainName())
                .coachNumber(ticket.getCoachNumber())
                .seatNumber(ticket.getSeatNumber())
                .startStation(ticket.getStartStation())
                .endStation(ticket.getEndStation())
                .departureTime(ticket.getDepartureTime())
                .travelDate(ticket.getTravelDate())
                .price(ticket.getPrice())
                .ticketType(ticket.getTicketType())
                .status(ticket.getStatus())
                .build();
    }

    private MyTicketView mapToMyTicket(TicketEntity ticket) {
        return MyTicketView.builder()
                .ticketId(ticket.getId())
                .trainName(ticket.getTrainName())
                .coachNumber(ticket.getCoachNumber())
                .seatNumber(ticket.getSeatNumber())
                .startStation(ticket.getStartStation())
                .endStation(ticket.getEndStation())
                .departureTime(ticket.getDepartureTime())
                .price(ticket.getPrice())
                .ticketType(ticket.getTicketType())
                .status(ticket.getStatus())
                .build();
    }
}
