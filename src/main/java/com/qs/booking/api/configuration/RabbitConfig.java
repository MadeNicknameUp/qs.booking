package com.qs.booking.api.configuration;

import com.qs.booking.api.dto.external.response.BookingResponseDto;
import com.qs.booking.api.dto.interservice.BookingOrderDto;
import com.qs.booking.api.dto.interservice.SpotOrderDto;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.spot-queue.name}")
    private String spotOrderQueueName;

    @Value("${rabbitmq.booking-queue.name}")
    private String bookingOrderQueueName;

    @Value("${rabbitmq.notification-queue.name}")
    private String notificationOrderQueueName;

    @Value("${rabbitmq.spot-queue.routing.key}")
    private String spotQueueRoutingKey;

    @Value("${rabbitmq.booking-queue.routing.key}")
    private String bookingQueueRoutingKey;

    @Value("${rabbitmq.notification-queue.routing.key}")
    private String notificationQueueRoutingKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

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
        return new Queue(spotOrderQueueName, true);
    }

    @Bean
    public Declarables bookingDeclarables(DirectExchange exchange) {
        Queue spotOrderQueue = new Queue(spotOrderQueueName, true);
        Queue bookingOrderQueue = new Queue(bookingOrderQueueName, true);
        Queue notificationOrderQueue = new Queue(notificationOrderQueueName, true);

        return new Declarables(
                spotOrderQueue,
                bookingOrderQueue,
                notificationOrderQueue,
                BindingBuilder.bind(spotOrderQueue)
                        .to(exchange())
                        .with(spotQueueRoutingKey),
                BindingBuilder.bind(bookingOrderQueue)
                        .to(exchange())
                        .with(bookingQueueRoutingKey),
                BindingBuilder.bind(notificationOrderQueue)
                        .to(exchange())
                        .with(notificationQueueRoutingKey)
        );
    }


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
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
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        return factory;
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {

        JacksonJsonMessageConverter converter =
                new JacksonJsonMessageConverter();

        DefaultJacksonJavaTypeMapper mapper =
                new DefaultJacksonJavaTypeMapper();

        mapper.setIdClassMapping(Map.of(
                "SpotOrderDto", SpotOrderDto.class,
                "BookingOrderDto", BookingOrderDto.class,
                "BookingResponseDto", BookingResponseDto.class
        ));

        converter.setJavaTypeMapper(mapper);

        return converter;
    }
}
