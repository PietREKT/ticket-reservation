package org.piet.ticketsbackend.tickets;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.tickets.dto.MyTicketView;
import org.piet.ticketsbackend.tickets.dto.TicketPurchaseRequest;
import org.piet.ticketsbackend.tickets.dto.TicketResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/ticket")
    public TicketResponse buyTicket(@Valid @RequestBody TicketPurchaseRequest req) {
        return ticketService.buyTicket(req);
    }

    @DeleteMapping("/ticket/{id}")
    public void cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
    }

    @GetMapping("/my-tickets")
    public List<MyTicketView> myTickets(@RequestParam Long passengerId) {
        return ticketService.getTicketsForPassenger(passengerId);
    }
}
