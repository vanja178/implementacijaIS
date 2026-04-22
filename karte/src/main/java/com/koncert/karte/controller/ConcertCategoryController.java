package com.koncert.karte.controller;

import com.koncert.karte.model.ConcertCategory;
import com.koncert.karte.service.ConcertCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/concert-categories")
@RequiredArgsConstructor
public class ConcertCategoryController {

    private final ConcertCategoryService concertCategoryService;

    @GetMapping
    public ResponseEntity<List<ConcertCategory>> getAll() {
        return ResponseEntity.ok(concertCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertCategory> getById(@PathVariable Long id) {
        return ResponseEntity.ok(concertCategoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ConcertCategory> create(@RequestBody ConcertCategory category) {
        return ResponseEntity.ok(concertCategoryService.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConcertCategory> update(@PathVariable Long id, @RequestBody ConcertCategory category) {
        category.setId(id);
        return ResponseEntity.ok(concertCategoryService.save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            concertCategoryService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}