package org.piet.ticketsbackend.tickets;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.tickets.dto.MyTicketView;
import org.piet.ticketsbackend.tickets.dto.TicketPurchaseRequest;
import org.piet.ticketsbackend.tickets.dto.TicketResponse;
import org.piet.ticketsbackend.tickets.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping("/ticket")
    public TicketResponse buyTicket(@Valid @RequestBody TicketPurchaseRequest request) {
        return ticketMapper.toResponse(ticketService.buyTicket(request));
    }

    // TicketEntity ma Long id, więc tutaj też Long
    @DeleteMapping("/ticket/{id}")
    public void cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
    }

    @GetMapping("/my-tickets")
    public PageDto myTickets(@RequestParam UUID passengerId,
                             PaginationDto paginationDto) {
        Page<?> page = ticketService.getTicketsForPassenger(passengerId, paginationDto);
        Page<MyTicketView> mapped = page.map(t -> ticketMapper.toMyTicketView((TicketEntity) t));
        return PageDto.create(mapped);
    }
}
