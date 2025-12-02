package org.piet.ticketsbackend.tickets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.passengers.PassengerEntity;
import org.piet.ticketsbackend.passengers.PassengerRepository;
import org.piet.ticketsbackend.tickets.client.RouteApiClient;
import org.piet.ticketsbackend.tickets.client.SeatApiClient;
import org.piet.ticketsbackend.tickets.dto.TicketPurchaseRequest;
import org.piet.ticketsbackend.tickets.service.TicketService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private RouteApiClient routeApiClient;

    @Mock
    private SeatApiClient seatApiClient;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void buyTicket_shouldCreateTicketWithCorrectPriceAndSeat() {
        UUID passengerId = UUID.randomUUID();
        PassengerEntity passenger = new PassengerEntity();
        passenger.setId(passengerId);
        passenger.setFirstName("Marcin");
        passenger.setLastName("Szczupak");
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));

        LocalDate travelDate = LocalDate.of(2025, 12, 1);

        RouteApiClient.RouteInfo routeInfo = new RouteApiClient.RouteInfo(
                1L,
                "WAW",
                "KRK",
                LocalDateTime.of(2025, 12, 1, 8, 0),
                new BigDecimal("100.00")
        );
        when(routeApiClient.getRouteInfo(1L, travelDate, "WAW", "KRK"))
                .thenReturn(routeInfo);

        SeatApiClient.SeatAllocationResponse seatResponse =
                new SeatApiClient.SeatAllocationResponse(
                        10L,           // trainId
                        5L,            // wagonId
                        15,            // seatNumber
                        travelDate,    // travelDate
                        "Train 10"     // trainName
                );
        when(seatApiClient.allocateSeat(10L, 5L, 1, travelDate))
                .thenReturn(seatResponse);

        TicketPurchaseRequest request = new TicketPurchaseRequest();
        request.setPassengerId(passengerId);
        request.setTrainId(10L);
        request.setWagonId(5L);
        request.setCoachNumber(1);
        request.setRouteId(1L);
        request.setStartStationCode("WAW");
        request.setEndStationCode("KRK");
        request.setTravelDate(travelDate);
        request.setTicketType(TicketType.DISCOUNT);

        when(ticketRepository.save(any(TicketEntity.class)))
                .thenAnswer(invocation -> {
                    TicketEntity t = invocation.getArgument(0);
                    t.setId(1L);
                    return t;
                });

        TicketEntity ticket = ticketService.buyTicket(request);

        assertNotNull(ticket.getId());
        assertEquals(passengerId, ticket.getPassenger().getId());
        assertEquals("Train 10", ticket.getTrainName());
        assertEquals(15, ticket.getSeatNumber());
        assertEquals(TicketType.DISCOUNT, ticket.getTicketType());
        assertEquals(TicketStatus.ACTIVE, ticket.getStatus());
        // 100 * 0.5 = 50, porównanie przez compareTo, żeby skala nie przeszkadzała
        assertEquals(0, ticket.getPrice().compareTo(new BigDecimal("50.00")));

        verify(ticketRepository, times(1)).save(any(TicketEntity.class));
    }

    @Test
    void buyTicket_shouldThrowWhenPassengerNotFound() {
        UUID passengerId = UUID.randomUUID();
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());

        TicketPurchaseRequest request = new TicketPurchaseRequest();
        request.setPassengerId(passengerId);

        assertThrows(NotFoundException.class, () -> ticketService.buyTicket(request));
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void buyTicket_shouldThrowWhenNoSeatAvailable() {
        UUID passengerId = UUID.randomUUID();
        PassengerEntity passenger = new PassengerEntity();
        passenger.setId(passengerId);
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));

        LocalDate date = LocalDate.of(2025, 12, 1);

        // NIE stubujemy routeApiClient tutaj – wyjątek poleci już na allocateSeat
        when(seatApiClient.allocateSeat(10L, 5L, 1, date))
                .thenThrow(new BadRequestException("No free seats in this wagon"));

        TicketPurchaseRequest request = new TicketPurchaseRequest();
        request.setPassengerId(passengerId);
        request.setTrainId(10L);
        request.setWagonId(5L);
        request.setCoachNumber(1);
        request.setRouteId(1L);
        request.setStartStationCode("WAW");
        request.setEndStationCode("KRK");
        request.setTravelDate(date);
        request.setTicketType(TicketType.NORMAL);

        assertThrows(BadRequestException.class, () -> ticketService.buyTicket(request));
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void cancelTicket_shouldSetStatusCanceledAndCallReleaseSeat() {
        TicketEntity ticket = new TicketEntity();
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.ACTIVE);
        ticket.setTrainId(10L);
        ticket.setWagonId(5L);
        ticket.setSeatNumber(15);
        ticket.setTravelDate(LocalDate.of(2025, 12, 1));

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ticketService.cancelTicket(1L);

        assertEquals(TicketStatus.CANCELED, ticket.getStatus());
        verify(ticketRepository, times(1)).save(ticket);
        verify(seatApiClient, times(1)).releaseSeat(
                10L, 5L, 15, LocalDate.of(2025, 12, 1)
        );
    }

    @Test
    void cancelTicket_shouldThrowWhenTicketNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ticketService.cancelTicket(99L));
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void getTicketsForPassenger_shouldDelegateToRepository() {
        UUID passengerId = UUID.randomUUID();
        PaginationDto pagination = new PaginationDto(0, 20);
        PageRequest pageable = PageRequest.of(0, 20);

        Page<TicketEntity> page = new PageImpl<>(
                java.util.List.of(new TicketEntity(), new TicketEntity())
        );

        when(ticketRepository.findByPassenger_Id(passengerId, pageable))
                .thenReturn(page);

        Page<TicketEntity> result = ticketService.getTicketsForPassenger(passengerId, pagination);

        assertEquals(2, result.getContent().size());
        verify(ticketRepository, times(1))
                .findByPassenger_Id(passengerId, pageable);
    }
}
