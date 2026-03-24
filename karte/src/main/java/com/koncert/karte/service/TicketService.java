package com.koncert.karte.service;

import com.koncert.karte.model.*;
import com.koncert.karte.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final ConcertRegionPriceRepository concertRegionPriceRepository;
    private final DiscountPeriodService discountPeriodService;

    public Ticket getByCodeAndEmail(String code, String email) {
        return ticketRepository.findByCodeAndEmail(code, email)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket createTicket(Ticket ticket, List<Long> regionPriceIds) {
        ticket.setCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        ticket.setStatus("ACTIVE");
        ticket.setCreatedAt(LocalDateTime.now());

        BigDecimal total = calculateTotal(ticket.getConcert().getId(), regionPriceIds);
        ticket.setTotalPrice(total);

        Ticket saved = ticketRepository.save(ticket);

        for (Long regionPriceId : regionPriceIds) {
            ConcertRegionPrice crp = concertRegionPriceRepository.findById(regionPriceId)
                    .orElseThrow(() -> new RuntimeException("Region price not found"));
            TicketSeat seat = new TicketSeat();
            seat.setTicket(saved);
            seat.setConcertRegionPrice(crp);
            seat.setSeatNumber(0);
            seat.setPricePaid(crp.getPrice());
            ticketSeatRepository.save(seat);
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setTicket(saved);
        promoCode.setCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        promoCode.setStatus("ACTIVE");
        promoCodeRepository.save(promoCode);

        return saved;
    }

    public Ticket cancelTicket(String code, String email) {
        Ticket ticket = getByCodeAndEmail(code, email);
        ticket.setStatus("CANCELLED");
        discountPeriodService.getByConcertId(ticket.getConcert().getId());
        promoCodeRepository.findByTicketId(ticket.getId()).ifPresent(pc -> {
            pc.setStatus("INVALID");
            promoCodeRepository.save(pc);
        });
        return ticketRepository.save(ticket);
    }

    private BigDecimal calculateTotal(Long concertId, List<Long> regionPriceIds) {
        BigDecimal total = BigDecimal.ZERO;
        boolean discount = discountPeriodService.isDiscountActive(concertId);

        for (Long id : regionPriceIds) {
            ConcertRegionPrice crp = concertRegionPriceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Region price not found"));
            total = total.add(crp.getPrice());
        }

        if (discount) {
            BigDecimal discountAmount = total.multiply(new BigDecimal("0.10"));
            total = total.subtract(discountAmount);
        }

        return total;
    }
}