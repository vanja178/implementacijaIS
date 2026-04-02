package com.koncert.karte.controller;

import com.koncert.karte.config.RabbitMQConfig;
import com.koncert.karte.dto.TicketResponse;
import com.koncert.karte.model.Ticket;
import com.koncert.karte.model.TicketSeat;
import com.koncert.karte.service.TicketSeatService;
import com.koncert.karte.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketSeatService ticketSeatService;
    private final RabbitTemplate rabbitTemplate;

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

    String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    String newPromoCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    Map<String, Object> message = new HashMap<>();
    message.put("ticket", ticket);
    message.put("regionPriceIds", regionPriceIds);
    message.put("promoCode", promoCode);
    message.put("code", code);
    message.put("newPromoCode", newPromoCode);

    rabbitTemplate.convertAndSend(RabbitMQConfig.TICKET_CREATE_QUEUE, message);

    Ticket tempTicket = new Ticket();
    tempTicket.setCode(code);
    return ResponseEntity.ok(new TicketResponse(tempTicket, newPromoCode));
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