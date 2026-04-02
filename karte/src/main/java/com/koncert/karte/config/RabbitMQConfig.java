package com.koncert.karte.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TICKET_QUEUE = "ticket-events";
    public static final String TICKET_CREATE_QUEUE = "ticket-create";

    @Bean
    public Queue ticketQueue() {
        return new Queue(TICKET_QUEUE, true);
    }

    @Bean
    public Queue ticketCreateQueue() {
        return new Queue(TICKET_CREATE_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}