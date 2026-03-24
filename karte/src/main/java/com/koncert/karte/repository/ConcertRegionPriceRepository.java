package com.koncert.karte.repository;

import com.koncert.karte.model.ConcertRegionPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConcertRegionPriceRepository extends JpaRepository<ConcertRegionPrice, Long> {
    List<ConcertRegionPrice> findByConcertId(Long concertId);
}