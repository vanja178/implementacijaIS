package com.portal.izvestavanje.controller;

import com.portal.izvestavanje.service.TicketStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001")
public class StatController {

    private final TicketStatService ticketStatService;

    @GetMapping("/concerts")
    public ResponseEntity<List<Object[]>> getCountByConcert() {
        return ResponseEntity.ok(ticketStatService.getCountByConcert());
    }

    @GetMapping("/locations")
    public ResponseEntity<List<Object[]>> getCountByLocation() {
        return ResponseEntity.ok(ticketStatService.getCountByLocation());
    }
}
