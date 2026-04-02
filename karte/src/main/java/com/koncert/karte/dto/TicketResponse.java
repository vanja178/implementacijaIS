package com.koncert.karte.dto;

import com.koncert.karte.model.Ticket;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class TicketResponse {
    private Ticket ticket;
    private String promoCode;
}