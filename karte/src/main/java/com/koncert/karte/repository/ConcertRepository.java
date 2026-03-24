package com.koncert.karte.repository;

import com.koncert.karte.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
    List<Concert> findByCategoryId(Long categoryId);
    List<Concert> findByLocationId(Long locationId);
}