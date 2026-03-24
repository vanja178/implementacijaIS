package com.koncert.karte.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seating_regions")
public class SeatingRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;
}