package com.koncert.karte.controller;

import com.koncert.karte.model.ConcertRegionPrice;
import com.koncert.karte.service.ConcertRegionPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/concert-region-prices")
@RequiredArgsConstructor
public class ConcertRegionPriceController {

    private final ConcertRegionPriceService concertRegionPriceService;

    @GetMapping
    public ResponseEntity<List<ConcertRegionPrice>> getAll() {
        return ResponseEntity.ok(concertRegionPriceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertRegionPrice> getById(@PathVariable Long id) {
        return ResponseEntity.ok(concertRegionPriceService.getById(id));
    }

    @GetMapping("/concert/{concertId}")
    public ResponseEntity<List<ConcertRegionPrice>> getByConcertId(@PathVariable Long concertId) {
        return ResponseEntity.ok(concertRegionPriceService.getByConcertId(concertId));
    }

    @PostMapping
    public ResponseEntity<ConcertRegionPrice> create(@RequestBody ConcertRegionPrice concertRegionPrice) {
        return ResponseEntity.ok(concertRegionPriceService.save(concertRegionPrice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConcertRegionPrice> update(@PathVariable Long id, @RequestBody ConcertRegionPrice concertRegionPrice) {
        concertRegionPrice.setId(id);
        return ResponseEntity.ok(concertRegionPriceService.save(concertRegionPrice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        concertRegionPriceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}