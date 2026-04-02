package com.koncert.karte.service;

import com.koncert.karte.model.Concert;
import com.koncert.karte.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

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
        concertRepository.deleteById(id);
    }
}