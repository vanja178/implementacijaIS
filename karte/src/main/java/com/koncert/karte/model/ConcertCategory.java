package com.koncert.karte.model;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "concert_categories")
public class ConcertCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
} 