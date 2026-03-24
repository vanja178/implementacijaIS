package com.koncert.karte.controller;

import com.koncert.karte.model.SeatingRegion;
import com.koncert.karte.service.SeatingRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seating-regions")
@RequiredArgsConstructor
public class SeatingRegionController {

    private final SeatingRegionService seatingRegionService;

    @GetMapping
    public ResponseEntity<List<SeatingRegion>> getAll() {
        return ResponseEntity.ok(seatingRegionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatingRegion> getById(@PathVariable Long id) {
        return ResponseEntity.ok(seatingRegionService.getById(id));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<SeatingRegion>> getByLocationId(@PathVariable Long locationId) {
        return ResponseEntity.ok(seatingRegionService.getByLocationId(locationId));
    }

    @PostMapping
    public ResponseEntity<SeatingRegion> create(@RequestBody SeatingRegion seatingRegion) {
        return ResponseEntity.ok(seatingRegionService.save(seatingRegion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatingRegion> update(@PathVariable Long id, @RequestBody SeatingRegion seatingRegion) {
        seatingRegion.setId(id);
        return ResponseEntity.ok(seatingRegionService.save(seatingRegion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seatingRegionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}