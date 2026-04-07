package com.koncert.karte.controller;

import com.koncert.karte.config.RabbitMQConfig;
import com.koncert.karte.dto.TicketResponse;
import com.koncert.karte.model.ConcertRegionPrice;
import com.koncert.karte.model.Ticket;
import com.koncert.karte.model.TicketSeat;
import com.koncert.karte.repository.ConcertRegionPriceRepository;
import com.koncert.karte.repository.TicketSeatRepository;
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
    private final ConcertRegionPriceRepository concertRegionPriceRepository;
    private final TicketSeatRepository ticketSeatRepository;

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
    public ResponseEntity<?> create(
            @RequestBody Ticket ticket,
            @RequestParam List<Long> regionPriceIds,
            @RequestParam(required = false) String promoCode) {

        // Grupisemo po regionId i brojimo koliko karata korisnik trazi
        Map<Long, Long> regionCounts = new HashMap<>();
        Map<Long, ConcertRegionPrice> regionPriceMap = new HashMap<>();

        for (Long regionPriceId : regionPriceIds) {
            ConcertRegionPrice crp = concertRegionPriceRepository.findById(regionPriceId)
                    .orElseThrow(() -> new RuntimeException("Region price not found"));
            Long regionId = crp.getRegion().getId();
            regionCounts.put(regionId, regionCounts.getOrDefault(regionId, 0L) + 1);
            regionPriceMap.put(regionId, crp);
        }

        // Proveravamo da li ima dovoljno mesta za svaki region
        for (Map.Entry<Long, Long> entry : regionCounts.entrySet()) {
            Long regionId = entry.getKey();
            Long requested = entry.getValue();
            ConcertRegionPrice crp = regionPriceMap.get(regionId);

            long capacity = crp.getRegion().getCapacity();
            long sold = ticketSeatRepository.countActiveByRegionId(regionId);

            if (sold + requested > capacity) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Nema dovoljno slobodnih mesta u regionu: " + crp.getRegion().getName()));
            }
        }

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