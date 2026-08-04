package com.qs.booking.api.contoller;

import com.qs.booking.api.dto.EventRequestDto;
import com.qs.booking.api.dto.EventResponseDto;
import com.qs.booking.api.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents(
            @RequestParam(name="page_number") Integer pageNumber,
            @RequestParam(name="page_size") Integer pageSize
    ) {
        return ResponseEntity
                .ok(eventService.getUpcomingEvents(pageNumber, pageSize));
    }

    @GetMapping("/{event_id}")
    public ResponseEntity<EventResponseDto> fetchEvent(@PathVariable UUID eventId) {
        return ResponseEntity
                .ok(eventService.fetchEvent(eventId));
    }

    @PostMapping("/{event_id}")
    public ResponseEntity<EventResponseDto> createEvent(@PathVariable UUID eventId,
                                                        @RequestBody EventRequestDto eventRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.createEvent(eventId, eventRequestDto));
    }

    @PatchMapping("/{event_id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable UUID eventId,
                                                        @RequestBody JsonNode eventRequestDto) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, eventRequestDto));
    }
}
