package com.koncert.karte.controller;

import com.koncert.karte.model.Ticket;
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

    @GetMapping
    public ResponseEntity<Ticket> getByCodeAndEmail(
            @RequestParam String code,
            @RequestParam String email) {
        return ResponseEntity.ok(ticketService.getByCodeAndEmail(code, email));
    }

    @PostMapping
    public ResponseEntity<Ticket> create(
            @RequestBody Ticket ticket,
            @RequestParam List<Long> regionPriceIds) {
        return ResponseEntity.ok(ticketService.createTicket(ticket, regionPriceIds));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Ticket> cancel(
            @RequestParam String code,
            @RequestParam String email) {
        return ResponseEntity.ok(ticketService.cancelTicket(code, email));
    }
}