package com.koncert.karte.service;

import com.koncert.karte.model.TicketSeat;
import com.koncert.karte.repository.TicketSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketSeatService {

    private final TicketSeatRepository ticketSeatRepository;

    public List<TicketSeat> getAll() {
        return ticketSeatRepository.findAll();
    }

    public TicketSeat getById(Long id) {
        return ticketSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket seat not found"));
    }

    public List<TicketSeat> getByTicketId(Long ticketId) {
        return ticketSeatRepository.findByTicketId(ticketId);
    }

    public TicketSeat save(TicketSeat ticketSeat) {
        return ticketSeatRepository.save(ticketSeat);
    }

    public void delete(Long id) {
        ticketSeatRepository.deleteById(id);
    }
}