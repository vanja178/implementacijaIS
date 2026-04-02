package com.koncert.karte.controller;

import com.koncert.karte.dto.TicketResponse;
import com.koncert.karte.model.Ticket;
import com.koncert.karte.model.TicketSeat;
import com.koncert.karte.service.TicketSeatService;
import com.koncert.karte.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketSeatService ticketSeatService;

    @GetMapping
    public ResponseEntity<Ticket> getByCodeAndEmail(
            @RequestParam String code,
            @RequestParam String email) {
        return ResponseEntity.ok(ticketService.getByCodeAndEmail(code, email));
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<TicketSeat>> getSeats(@PathVariable Long id) {
        return ResponseEntity.ok(ticketSeatService.getByTicketId(id));
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(
            @RequestBody Ticket ticket,
            @RequestParam List<Long> regionPriceIds,
            @RequestParam(required = false) String promoCode) {
        return ResponseEntity.ok(ticketService.createTicket(ticket, regionPriceIds, promoCode));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Ticket> cancel(
            @RequestParam String code,
            @RequestParam String email) {
        return ResponseEntity.ok(ticketService.cancelTicket(code, email));
    }

    @PostMapping("/add-seats")
    public ResponseEntity<Ticket> addSeats(
            @RequestParam String code,
            @RequestParam String email,
            @RequestParam List<Long> regionPriceIds) {
        return ResponseEntity.ok(ticketService.addSeats(code, email, regionPriceIds));
    }

    @PostMapping("/remove-seats")
    public ResponseEntity<Ticket> removeSeats(
            @RequestParam String code,
            @RequestParam String email,
            @RequestParam List<Long> seatIds) {
        return ResponseEntity.ok(ticketService.removeSeats(code, email, seatIds));
    }
}