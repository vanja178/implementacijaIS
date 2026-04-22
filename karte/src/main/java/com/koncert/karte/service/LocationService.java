package com.koncert.karte.service;

import com.koncert.karte.model.Location;
import com.koncert.karte.repository.ConcertRepository;
import com.koncert.karte.repository.LocationRepository;
import com.koncert.karte.repository.SeatingRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final ConcertRepository concertRepository;
    private final SeatingRegionRepository seatingRegionRepository;

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location getById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public void delete(Long id) {
        if (!concertRepository.findByLocationId(id).isEmpty()) {
            throw new RuntimeException("Ne možete obrisati lokaciju koja ima zakazane koncerte.");
        }
        if (!seatingRegionRepository.findByLocationId(id).isEmpty()) {
            throw new RuntimeException("Ne možete obrisati lokaciju koja ima regione sedenja.");
        }
        locationRepository.deleteById(id);
    }
}