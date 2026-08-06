package com.qs.booking.api.service;

import com.qs.booking.api.mapper.EventDtoMapper;
import com.qs.booking.api.dto.external.EventRequestDto;
import com.qs.booking.api.dto.external.EventResponseDto;
import com.qs.booking.api.error.unit.AccountNotFoundException;
import com.qs.booking.api.error.unit.EventNotFoundException;
import com.qs.booking.api.mapper.SpotDtoMapper;
import com.qs.booking.store.model.Account;
import com.qs.booking.store.model.Event;
import com.qs.booking.store.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AccountService accountService;
    private final EventDtoMapper eventDtoMapper;
    private final SpotDtoMapper spotDtoMapper;
    private final ObjectMapper objectMapper;
    private final EventCaching eventCaching;
    private final EventProducer eventProducer;

    public List<EventResponseDto> getUpcomingEvents(Integer pageNumber, Integer pageSize) {

        List<Event> cachedUpcomingEvents = eventCaching.getFeed(pageNumber);

        if (!cachedUpcomingEvents.isEmpty()) {
            return cachedUpcomingEvents
                    .stream()
                    .map(eventDtoMapper::toDto)
                    .toList();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Event> upcomingEvents = eventRepository.findAllUpcomingEvents(pageable);

        // TODO: Replace it with @Scheduled job???
        Map<String, Event> events = new HashMap<>();

        upcomingEvents.stream().forEach((event) -> events.put(event.getId().toString(), event));

        eventCaching.cache(pageNumber, events);

        return upcomingEvents
                .stream()
                .map(eventDtoMapper::toDto)
                .toList();
    }

    public EventResponseDto fetchEvent(UUID eventId) {

        Event fetchedEvent = eventCaching.get(eventId.toString()).orElseGet(() -> {

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new EventNotFoundException("Event with id: %s not found.".formatted(eventId)));

            eventCaching.cache(eventId.toString(), event);

            return event;
        });

        return eventDtoMapper.toDto(fetchedEvent);
    }

    @Transactional
    public EventResponseDto createEvent(UUID accountId, EventRequestDto eventRequestDto) {

        // TODO: replace with gRPC later
        Account fetchedAccount = accountService.internalFetchAccount(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with id: %s not found.".formatted(accountId)));

        Event mappedEvent = eventDtoMapper.toEntity(eventRequestDto);
        mappedEvent.setAuthorId(fetchedAccount.getId());

        final Event savedEvent = eventRepository.saveAndFlush(mappedEvent);

        eventProducer.postOrder(spotDtoMapper.toInternalDto(savedEvent.getId(), eventRequestDto));

        return eventDtoMapper.toDto(savedEvent);
    }

    @Transactional
    public EventResponseDto updateEvent(UUID eventId, JsonNode brandNewAccountPart) {

        Event fetchedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: %s cannot not found.".formatted(eventId)));

        objectMapper.readerForUpdating(fetchedEvent).readValue(brandNewAccountPart);

        validateEventData(fetchedEvent);

        final Event savedEvent = eventRepository.saveAndFlush(fetchedEvent);

        eventCaching.evict(eventId.toString());

        return eventDtoMapper.toDto(savedEvent);
    }

    private void validateEventData(Event event) {
        // TODO: Create data validation strategy & requirements
    }
}
