package com.koncert.karte.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@Entity
@Table(name = "concert_region_prices")
public class ConcertRegionPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    @JsonIgnoreProperties({"concertRegionPrices", "location", "category"})
    private Concert concert;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    @JsonIgnoreProperties({"concertRegionPrices", "location"})
    private SeatingRegion region;

    @Column(nullable = false)
    private BigDecimal price;
}