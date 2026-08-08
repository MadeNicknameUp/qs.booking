package com.qs.booking.api.contoller;

import com.qs.booking.api.dto.external.request.patch.EventPatchDto;
import com.qs.booking.api.dto.external.request.post.EventPostDto;
import com.qs.booking.api.dto.external.response.EventResponseDto;
import com.qs.booking.api.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<EventResponseDto> fetchEvent(@PathVariable(name="event_id") UUID eventId) {
        return ResponseEntity
                .ok(eventService.fetchEvent(eventId));
    }

    @PostMapping("/{account_id}")
    public ResponseEntity<EventResponseDto> createEvent(@PathVariable(name="account_id") UUID accountId,
                                                        @RequestBody EventPostDto eventPostDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.createEvent(accountId, eventPostDto));
    }

    @PatchMapping("/{event_id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable(name="event_id") UUID eventId,
                                                        @RequestBody EventPatchDto eventRequestDto) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, eventRequestDto));
    }
}
