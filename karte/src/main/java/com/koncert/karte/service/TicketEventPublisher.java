package com.koncert.karte.service;

import com.koncert.karte.config.RabbitMQConfig;
import com.koncert.karte.repository.ConcertRepository;
import com.koncert.karte.repository.TicketRepository;
import com.koncert.karte.repository.TicketSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final TicketRepository ticketRepository;
    private final ConcertRepository concertRepository;
    private final TicketSeatRepository ticketSeatRepository;

    public void publishTicketCreated(Long ticketId) {
        Map<String, Object> message = buildMessage("TICKET_CREATED", ticketId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TICKET_QUEUE, message);
    }

    public void publishTicketUpdated(Long ticketId) {
        Map<String, Object> message = buildMessage("TICKET_UPDATED", ticketId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TICKET_QUEUE, message);
    }

    public void publishTicketCancelled(Long ticketId) {
        Map<String, Object> message = buildMessage("TICKET_CANCELLED", ticketId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TICKET_QUEUE, message);
    }

    private Map<String, Object> buildMessage(String eventType, Long ticketId) {
        Map<String, Object> message = new HashMap<>();
        message.put("event", eventType);
        message.put("ticketId", ticketId);

        ticketRepository.findById(ticketId).ifPresent(ticket -> {
            Long concertId = ticket.getConcert().getId();
            concertRepository.findById(concertId).ifPresent(concert -> {
                message.put("concertName", concert.getName());
                message.put("locationName", concert.getLocation() != null ? concert.getLocation().getName() : "");
            });
            long seatCount = ticketSeatRepository.findByTicketId(ticketId).size();
            message.put("seatCount", seatCount);
        });

        return message;
    }
}