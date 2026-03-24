package com.koncert.karte.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ticket_seats")
public class TicketSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "concert_region_price_id", nullable = false)
    private ConcertRegionPrice concertRegionPrice;

    @Column(nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    private BigDecimal pricePaid;
}