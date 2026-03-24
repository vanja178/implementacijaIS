package com.koncert.karte.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "concert_categories")
public class ConcertCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
} 