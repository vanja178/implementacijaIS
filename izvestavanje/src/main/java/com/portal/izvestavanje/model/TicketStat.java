package com.portal.izvestavanje.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ticket_stats")
public class TicketStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ticketId;
    private String concertName;
    private String locationName;
    private String status;
    private String eventType;
    private Long seatCount;
}