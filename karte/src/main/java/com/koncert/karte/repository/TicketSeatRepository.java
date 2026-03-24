package com.koncert.karte.repository;

import com.koncert.karte.model.TicketSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketSeatRepository extends JpaRepository<TicketSeat, Long> {
    List<TicketSeat> findByTicketId(Long ticketId);
}