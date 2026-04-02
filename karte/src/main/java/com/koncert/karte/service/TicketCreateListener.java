package com.koncert.karte.service;

import com.koncert.karte.config.RabbitMQConfig;
import com.koncert.karte.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TicketCreateListener {

    private final TicketService ticketService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.TICKET_CREATE_QUEUE)
    public void handleTicketCreate(Map<String, Object> message) {
        try {
            Ticket ticket = objectMapper.convertValue(message.get("ticket"), Ticket.class);
            List<Integer> regionPriceIdsRaw = (List<Integer>) message.get("regionPriceIds");
            List<Long> regionPriceIds = regionPriceIdsRaw.stream().map(Long::valueOf).toList();
            String promoCode = (String) message.get("promoCode");
            String code = (String) message.get("code");
            String newPromoCode = (String) message.get("newPromoCode");

            ticketService.createTicket(ticket, regionPriceIds, promoCode, code, newPromoCode);
        } catch (Exception e) {
            System.err.println("Greška pri obradi karte: " + e.getMessage());
        }
    }
}