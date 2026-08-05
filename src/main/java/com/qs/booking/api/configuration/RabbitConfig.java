package com.qs.booking.api.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.spot-queue.name}")
    private String queue;

    @Value("${rabbitmq.spot-queue.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.config.host}")
    private String host;

    @Value("${rabbitmq.config.port}")
    private Integer port;

    @Value("${rabbitmq.config.username}")
    private String username;

    @Value("${rabbitmq.config.password}")
    private String password;

    @Value("${rabbitmq.config.virtual-host}")
    private String virtualHost;

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        // Simple routing (Router = Exchange, Queue = destination, routingKey = path)
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new JacksonJsonMessageConverter());
        return template;
    }
}
