package com.koncert.karte.controller;

import com.koncert.karte.model.Currency;
import com.koncert.karte.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Currency> create(@RequestBody Currency currency) {
        return ResponseEntity.ok(currencyService.save(currency));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> update(@PathVariable Long id, @RequestBody Currency currency) {
        currency.setId(id);
        return ResponseEntity.ok(currencyService.save(currency));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        currencyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}