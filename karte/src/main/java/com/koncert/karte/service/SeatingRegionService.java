package com.koncert.karte.service;

import com.koncert.karte.model.SeatingRegion;
import com.koncert.karte.repository.SeatingRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatingRegionService {

    private final SeatingRegionRepository seatingRegionRepository;

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
        seatingRegionRepository.deleteById(id);
    }
}