package com.koncert.karte.service;

import com.koncert.karte.dto.TicketResponse;
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
    private final TicketEventPublisher ticketEventPublisher;

    public Ticket getByCodeAndEmail(String code, String email) {
        return ticketRepository.findByCodeAndEmail(code, email)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public TicketResponse createTicket(Ticket ticket, List<Long> regionPriceIds, String promoCode, String code, String newPromoCode) {
        ticket.setCode(code);
        ticket.setStatus("ACTIVE");
        ticket.setCreatedAt(LocalDateTime.now());

        BigDecimal total = calculateTotal(ticket.getConcert().getId(), regionPriceIds);

        if (promoCode != null && !promoCode.isEmpty()) {
            PromoCode pc = promoCodeRepository.findByCode(promoCode).orElse(null);
            if (pc != null && pc.getStatus().equals("ACTIVE")) {
                BigDecimal promoDiscount = total.multiply(new BigDecimal("0.05"));
                total = total.subtract(promoDiscount);
                pc.setStatus("USED");
                pc.setUsedAt(LocalDateTime.now());
                promoCodeRepository.save(pc);
            }
        }

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

        PromoCode pc = new PromoCode();
        pc.setTicket(saved);
        pc.setCode(newPromoCode);
        pc.setStatus("ACTIVE");
        promoCodeRepository.save(pc);

        ticketEventPublisher.publishTicketCreated(saved.getId());

        return new TicketResponse(saved, newPromoCode);
    }

    public Ticket addSeats(String code, String email, List<Long> regionPriceIds) {
        Ticket ticket = getByCodeAndEmail(code, email);

        if (ticket.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Karta je otkazana i ne može se izmeniti.");
        }

        BigDecimal additional = calculateTotal(ticket.getConcert().getId(), regionPriceIds);
        ticket.setTotalPrice(ticket.getTotalPrice().add(additional));

        for (Long regionPriceId : regionPriceIds) {
            ConcertRegionPrice crp = concertRegionPriceRepository.findById(regionPriceId)
                    .orElseThrow(() -> new RuntimeException("Region price not found"));
            TicketSeat seat = new TicketSeat();
            seat.setTicket(ticket);
            seat.setConcertRegionPrice(crp);
            seat.setSeatNumber(0);
            seat.setPricePaid(crp.getPrice());
            ticketSeatRepository.save(seat);
        }

        ticketSeatRepository.flush();
        Ticket updated = ticketRepository.save(ticket);
        ticketEventPublisher.publishTicketUpdated(updated.getId());
        return updated;
    }

    public Ticket removeSeats(String code, String email, List<Long> seatIds) {
        Ticket ticket = getByCodeAndEmail(code, email);

        if (ticket.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Karta je otkazana i ne može se izmeniti.");
        }

        for (Long seatId : seatIds) {
            TicketSeat seat = ticketSeatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            ticket.setTotalPrice(ticket.getTotalPrice().subtract(seat.getPricePaid()));
            ticketSeatRepository.delete(seat);
        }

        ticketSeatRepository.flush();
        Ticket updated = ticketRepository.save(ticket);
        ticketEventPublisher.publishTicketUpdated(updated.getId());
        return updated;
    }

    public Ticket cancelTicket(String code, String email) {
        Ticket ticket = getByCodeAndEmail(code, email);
        ticket.setStatus("CANCELLED");
        discountPeriodService.getByConcertId(ticket.getConcert().getId());
        promoCodeRepository.findByTicketId(ticket.getId()).ifPresent(pc -> {
            pc.setStatus("INVALID");
            promoCodeRepository.save(pc);
        });
        Ticket cancelled = ticketRepository.save(ticket);
        ticketEventPublisher.publishTicketCancelled(cancelled.getId());
        return cancelled;
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