package com.koncert.karte.service;

import com.koncert.karte.model.Currency;
import com.koncert.karte.repository.CurrencyRepository;
import com.koncert.karte.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final TicketRepository ticketRepository;

    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    public Currency getById(Long id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found"));
    }

    public Currency getByCode(String code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Currency not found"));
    }

    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    public void delete(Long id) {
        if (!ticketRepository.findByCurrencyId(id).isEmpty()) {
            throw new RuntimeException("Ne mozete obrisati valutu koja se koristi u kartama.");
        }
        currencyRepository.deleteById(id);
    }
}