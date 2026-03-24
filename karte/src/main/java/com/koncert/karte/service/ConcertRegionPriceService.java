package com.koncert.karte.service;

import com.koncert.karte.model.ConcertRegionPrice;
import com.koncert.karte.repository.ConcertRegionPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertRegionPriceService {

    private final ConcertRegionPriceRepository concertRegionPriceRepository;

    public List<ConcertRegionPrice> getAll() {
        return concertRegionPriceRepository.findAll();
    }

    public ConcertRegionPrice getById(Long id) {
        return concertRegionPriceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert region price not found"));
    }

    public List<ConcertRegionPrice> getByConcertId(Long concertId) {
        return concertRegionPriceRepository.findByConcertId(concertId);
    }

    public ConcertRegionPrice save(ConcertRegionPrice concertRegionPrice) {
        return concertRegionPriceRepository.save(concertRegionPrice);
    }

    public void delete(Long id) {
        concertRegionPriceRepository.deleteById(id);
    }
}