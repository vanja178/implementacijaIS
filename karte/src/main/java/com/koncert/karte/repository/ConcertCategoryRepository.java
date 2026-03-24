package com.koncert.karte.repository;

import com.koncert.karte.model.ConcertCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertCategoryRepository extends JpaRepository<ConcertCategory, Long> {
}