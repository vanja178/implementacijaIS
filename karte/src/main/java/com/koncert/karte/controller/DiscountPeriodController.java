package com.koncert.karte.controller;

import com.koncert.karte.model.DiscountPeriod;
import com.koncert.karte.service.DiscountPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discount-periods")
@RequiredArgsConstructor
public class DiscountPeriodController {

    private final DiscountPeriodService discountPeriodService;

    @GetMapping
    public ResponseEntity<List<DiscountPeriod>> getAll() {
        return ResponseEntity.ok(discountPeriodService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountPeriod> getById(@PathVariable Long id) {
        return ResponseEntity.ok(discountPeriodService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DiscountPeriod> create(@RequestBody DiscountPeriod discountPeriod) {
        return ResponseEntity.ok(discountPeriodService.save(discountPeriod));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountPeriod> update(@PathVariable Long id, @RequestBody DiscountPeriod discountPeriod) {
        discountPeriod.setId(id);
        return ResponseEntity.ok(discountPeriodService.save(discountPeriod));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        discountPeriodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}