package com.portal.izvestavanje.listener;

import com.portal.izvestavanje.model.TicketStat;
import com.portal.izvestavanje.service.TicketStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TicketEventListener {

    private final TicketStatService ticketStatService;

    @RabbitListener(queues = "ticket-events")
    public void handleTicketEvent(Map<String, Object> event) {
        String eventType = (String) event.get("event");
        Long ticketId = ((Number) event.get("ticketId")).longValue();
        String concertName = (String) event.getOrDefault("concertName", "");
        String locationName = (String) event.getOrDefault("locationName", "");
        Long seatCount = event.get("seatCount") != null ? ((Number) event.get("seatCount")).longValue() : 1L;

        if ("TICKET_CREATED".equals(eventType)) {
            TicketStat stat = new TicketStat();
            stat.setTicketId(ticketId);
            stat.setEventType(eventType);
            stat.setStatus("ACTIVE");
            stat.setConcertName(concertName);
            stat.setLocationName(locationName);
            stat.setSeatCount(seatCount);
            ticketStatService.save(stat);
        } else if ("TICKET_UPDATED".equals(eventType)) {
            ticketStatService.updateStatus(ticketId, "ACTIVE");
        } else if ("TICKET_CANCELLED".equals(eventType)) {
            ticketStatService.updateStatus(ticketId, "CANCELLED");
        }
    }
}