package com.koncert.karte.service;

import com.koncert.karte.model.Concert;
import com.koncert.karte.repository.ConcertRegionPriceRepository;
import com.koncert.karte.repository.ConcertRepository;
import com.koncert.karte.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final TicketRepository ticketRepository;
    private final ConcertRegionPriceRepository concertRegionPriceRepository;

    @Cacheable("concerts")
    public List<Concert> getAll() {
        return concertRepository.findAll();
    }

    public Concert getById(Long id) {
        return concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found"));
    }

    public List<Concert> getByCategoryId(Long categoryId) {
        return concertRepository.findByCategoryId(categoryId);
    }

    public List<Concert> getByLocationId(Long locationId) {
        return concertRepository.findByLocationId(locationId);
    }

    @CacheEvict(value = "concerts", allEntries = true)
    public Concert save(Concert concert) {
        return concertRepository.save(concert);
    }

    @CacheEvict(value = "concerts", allEntries = true)
    public void delete(Long id) {
        if (!ticketRepository.findByConcertId(id).isEmpty()) {
            throw new RuntimeException("Ne mozete obrisati koncert koji ima kupljene karte.");
        }
        if (!concertRegionPriceRepository.findByConcertId(id).isEmpty()) {
            throw new RuntimeException("Ne mozete obrisati koncert koji ima definisane cene karata.");
        }
        concertRepository.deleteById(id);
    }
}