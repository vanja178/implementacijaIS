package com.koncert.karte.service;

import com.koncert.karte.model.SeatingRegion;
import com.koncert.karte.repository.ConcertRegionPriceRepository;
import com.koncert.karte.repository.SeatingRegionRepository;
import com.koncert.karte.repository.TicketSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatingRegionService {

    private final SeatingRegionRepository seatingRegionRepository;
    private final ConcertRegionPriceRepository concertRegionPriceRepository;
    private final TicketSeatRepository ticketSeatRepository;

    public List<SeatingRegion> getAll() {
        return seatingRegionRepository.findAll();
    }

    public SeatingRegion getById(Long id) {
        return seatingRegionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seating region not found"));
    }

    public List<SeatingRegion> getByLocationId(Long locationId) {
        return seatingRegionRepository.findByLocationId(locationId);
    }

    public SeatingRegion save(SeatingRegion seatingRegion) {
        return seatingRegionRepository.save(seatingRegion);
    }

    public void delete(Long id) {
        long activeSeats = ticketSeatRepository.countActiveByRegionId(id);
        if (activeSeats > 0) {
            throw new RuntimeException("Ne mozete obrisati region koji ima kupljene karte.");
        }
        seatingRegionRepository.deleteById(id);
    }
}