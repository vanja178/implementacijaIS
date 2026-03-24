package com.koncert.karte.service;

import com.koncert.karte.model.ConcertCategory;
import com.koncert.karte.repository.ConcertCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertCategoryService {

    private final ConcertCategoryRepository concertCategoryRepository;

    public List<ConcertCategory> getAll() {
        return concertCategoryRepository.findAll();
    }

    public ConcertCategory getById(Long id) {
        return concertCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert category not found"));
    }

    public ConcertCategory save(ConcertCategory category) {
        return concertCategoryRepository.save(category);
    }

    public void delete(Long id) {
        concertCategoryRepository.deleteById(id);
    }
}