package com.portal.izvestavanje.repository;

import com.portal.izvestavanje.model.TicketStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketStatRepository extends JpaRepository<TicketStat, Long> {

    @Query("SELECT t.concertName, SUM(t.seatCount) FROM TicketStat t WHERE t.status = 'ACTIVE' GROUP BY t.concertName")
    List<Object[]> countByConcert();

    @Query("SELECT t.locationName, SUM(t.seatCount) FROM TicketStat t WHERE t.status = 'ACTIVE' GROUP BY t.locationName")
    List<Object[]> countByLocation();
}