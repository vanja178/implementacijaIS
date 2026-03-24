package com.koncert.karte.repository;

import com.koncert.karte.model.DiscountPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DiscountPeriodRepository extends JpaRepository<DiscountPeriod, Long> {
    Optional<DiscountPeriod> findByConcertId(Long concertId);
}