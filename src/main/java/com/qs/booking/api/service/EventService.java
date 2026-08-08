package com.qs.booking.api.service;

import com.qs.booking.api.dto.external.request.patch.EventPatchDto;
import com.qs.booking.api.error.unit.InvalidParameterException;
import com.qs.booking.api.mapper.EventDtoMapper;
import com.qs.booking.api.dto.external.request.post.EventPostDto;
import com.qs.booking.api.dto.external.response.EventResponseDto;
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

import java.time.Instant;
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
                    .orElseThrow(() -> new EventNotFoundException(
                            "Event with id: %s not found.".formatted(eventId),
                            "/api/v1/events/%s".formatted(eventId)
                    ));

            eventCaching.cache(eventId.toString(), event);

            return event;
        });

        return eventDtoMapper.toDto(fetchedEvent);
    }

    @Transactional
    public EventResponseDto createEvent(UUID accountId, EventPostDto eventPostDto) {

        // TODO: replace with gRPC later
        Account fetchedAccount = accountService.internalFetchAccount(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id: %s not found.".formatted(accountId),
                        "/api/v1/events/%s".formatted(accountId)
                ));

        Event mappedEvent = eventDtoMapper.toEntity(eventPostDto);
        mappedEvent.setAuthorId(fetchedAccount.getId());

        final Event savedEvent = eventRepository.saveAndFlush(mappedEvent);

        eventProducer.postOrder(spotDtoMapper.toInternalDto(savedEvent.getId(), eventPostDto));

        return eventDtoMapper.toDto(savedEvent);
    }

    // TODO: Notify everyone, who has relevant bookings, about event details update
    @Transactional
    public EventResponseDto updateEvent(UUID eventId, EventPatchDto eventPatchDto) {

        String errorPath = String.format("api/v1/events/%s", eventId);

        Event fetchedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(
                        "Invalid event_id: Event with id: %s cannot not found.".formatted(eventId),
                        errorPath
                ));

        validatePatchRequest(fetchedEvent, eventPatchDto, errorPath);

        final Event savedEvent = eventRepository.saveAndFlush(fetchedEvent);

        eventCaching.evict(eventId.toString());

        return eventDtoMapper.toDto(savedEvent);
    }

    public void validatePatchRequest(Event event, EventPatchDto eventPatchDto, String errorPath) {

        eventPatchDto.getName().ifPresent((name) -> {
            if (name.matches("^[\\p{L} '-]+$")) {
                event.setName(name);
            } else {
                throw new InvalidParameterException("Invalid name: Name contains invalid characters.", errorPath);
            }
        });
        eventPatchDto.getDescription().ifPresent((description) -> {
            if (description.length() > 2000) {
                event.setDescription(description);
            } else {
                throw new InvalidParameterException(
                        "Invalid description: Description is way too long. Maximum size is 2000 characters.",
                        errorPath
                );
            }
        });
        eventPatchDto.getStartingDate().ifPresent((startingDate) -> {
            Instant endingDate = eventPatchDto.getEndingDate().orElse(event.getEndingDate());
            if (endingDate.isAfter(startingDate)) {
                event.setStartingDate(startingDate);
            } else {
                throw new InvalidParameterException(
                        "Invalid starting_date: Ending date must be later then starting date.",
                        errorPath
                );
            }
        });
        eventPatchDto.getEndingDate().ifPresent((endingDate) -> {
            if (endingDate.isAfter(event.getStartingDate())) {
                event.setEndingDate(endingDate);
            } else {
                throw new InvalidParameterException(
                        "Invalid ending_date: Ending date must be later then starting date.",
                        errorPath
                );
            }
        });
        // TODO: gRPC call to Booking-service in order to know how many spots are already booked
//        eventPatchDto.getSpotsAmount().ifPresent((spotsAmount) -> {
//            if () {
//                fetchedEvent.setSpotsAmount(spotsAmount);
//            } else {
//                throw new InvalidParameterException(
//                      "Invalid spot_amount: Spot amount must exceed the amount of currently booked spots.",
//                      errorPath);
//            }
//        });
        eventPatchDto.getPictureUrl().ifPresent(event::setPictureUrl);
    }
}
