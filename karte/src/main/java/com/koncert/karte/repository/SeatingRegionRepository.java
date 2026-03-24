package com.koncert.karte.repository;

import com.koncert.karte.model.SeatingRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeatingRegionRepository extends JpaRepository<SeatingRegion, Long> {
    List<SeatingRegion> findByLocationId(Long locationId);
}