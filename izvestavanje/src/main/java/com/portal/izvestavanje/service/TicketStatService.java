package com.portal.izvestavanje.service;

import com.portal.izvestavanje.model.TicketStat;
import com.portal.izvestavanje.repository.TicketStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketStatService {

    private final TicketStatRepository ticketStatRepository;

    public List<Object[]> getCountByConcert() {
        return ticketStatRepository.countByConcert();
    }

    public List<Object[]> getCountByLocation() {
        return ticketStatRepository.countByLocation();
    }

    public void save(TicketStat ticketStat) {
        ticketStatRepository.save(ticketStat);
    }

    public void updateStatus(Long ticketId, String status) {
        ticketStatRepository.findAll().stream()
                .filter(t -> t.getTicketId().equals(ticketId))
                .forEach(t -> {
                    t.setStatus(status);
                    ticketStatRepository.save(t);
                });
    }

    public void updateSeatCount(Long ticketId, Long seatCount) {
        ticketStatRepository.findAll().stream()
                .filter(t -> t.getTicketId().equals(ticketId))
                .forEach(t -> {
                    t.setSeatCount(seatCount);
                    ticketStatRepository.save(t);
                });
    }
}