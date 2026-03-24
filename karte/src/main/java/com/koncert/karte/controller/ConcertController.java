package com.koncert.karte.controller;

import com.koncert.karte.model.Concert;
import com.koncert.karte.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping
    public ResponseEntity<List<Concert>> getAll() {
        return ResponseEntity.ok(concertService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concert> getById(@PathVariable Long id) {
        return ResponseEntity.ok(concertService.getById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Concert>> getByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(concertService.getByCategoryId(categoryId));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Concert>> getByLocationId(@PathVariable Long locationId) {
        return ResponseEntity.ok(concertService.getByLocationId(locationId));
    }

    @PostMapping
    public ResponseEntity<Concert> create(@RequestBody Concert concert) {
        return ResponseEntity.ok(concertService.save(concert));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concert> update(@PathVariable Long id, @RequestBody Concert concert) {
        concert.setId(id);
        return ResponseEntity.ok(concertService.save(concert));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        concertService.delete(id);
        return ResponseEntity.noContent().build();
    }
}