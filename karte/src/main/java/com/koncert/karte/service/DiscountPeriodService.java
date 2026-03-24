package com.koncert.karte.service;

import com.koncert.karte.model.DiscountPeriod;
import com.koncert.karte.repository.DiscountPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountPeriodService {

    private final DiscountPeriodRepository discountPeriodRepository;

    public List<DiscountPeriod> getAll() {
        return discountPeriodRepository.findAll();
    }

    public DiscountPeriod getById(Long id) {
        return discountPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount period not found"));
    }

    public Optional<DiscountPeriod> getByConcertId(Long concertId) {
        return discountPeriodRepository.findByConcertId(concertId);
    }

    public boolean isDiscountActive(Long concertId) {
        return discountPeriodRepository.findByConcertId(concertId)
                .map(dp -> !LocalDate.now().isAfter(dp.getValidUntil()))
                .orElse(false);
    }

    public DiscountPeriod save(DiscountPeriod discountPeriod) {
        return discountPeriodRepository.save(discountPeriod);
    }

    public void delete(Long id) {
        discountPeriodRepository.deleteById(id);
    }
}