package com.qs.booking.api.service;

import com.qs.booking.store.model.Event;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventCaching {

    private final RedisTemplate<String, Event> redisTemplate;

    private HashOperations<String, String, Event> hashOps;

    private static final String PAGE_KEY_PREFIX = "page:";
    private static final String EVENT_KEY_PREFIX = "event:";

    @PostConstruct
    private void init() {
        this.hashOps = redisTemplate.opsForHash();
    }

    @Transactional
    public void cache(Integer pageNumber, Map<String, Event> events) {

        String key = PAGE_KEY_PREFIX + pageNumber;
        hashOps.putAll(key, events);
        redisTemplate.expire(key, Duration.ofSeconds(60));
    }

    public List<Event> getFeed(Integer pageNumber) {

        try {
            return hashOps.entries(PAGE_KEY_PREFIX + pageNumber).values().stream().toList();
        } catch (RedisConnectionFailureException ex) {
            // TODO: Add logging (in entire project, xd)
            return Collections.emptyList();
        }
    }

    public void cache(String eventId, Event event) {

        redisTemplate.opsForValue().set(EVENT_KEY_PREFIX + eventId, event, Duration.ofMinutes(30));
    }

    public Optional<Event> get(String eventId) {

        return Optional.ofNullable(redisTemplate.opsForValue().get(EVENT_KEY_PREFIX + eventId));
    }

    public void evict(String eventId) {
        redisTemplate.delete(EVENT_KEY_PREFIX + eventId);
    }
}
