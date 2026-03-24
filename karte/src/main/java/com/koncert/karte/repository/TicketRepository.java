package com.koncert.karte.repository;

import com.koncert.karte.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByCodeAndEmail(String code, String email);
    Optional<Ticket> findByCode(String code);
}
