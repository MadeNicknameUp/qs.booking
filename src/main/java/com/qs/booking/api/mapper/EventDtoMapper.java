package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.external.EventRequestDto;
import com.qs.booking.api.dto.external.EventResponseDto;
import com.qs.booking.store.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class EventDtoMapper {

    public EventResponseDto toDto(Event event) {

        return EventResponseDto
                .builder()
                .id(event.getId().toString())
                .pictureUrl(event.getPictureUrl())
                .name(event.getName())
                .description(event.getDescription())
                .startingDate(event.getStartingDate().toString())
                .endingDate(event.getEndingDate().toString())
                .authorId(event.getAuthorId().toString())
                .spotsAmount(event.getSpotsAmount())
                .creationTimestamp(event.getCreationTimestamp().toString())
                .updateTimestamp(event.getCreationTimestamp().toString())
                .build();
    }

    public Event toEntity(EventRequestDto eventRequestDto) {

        Event event = new Event();
        event.setPictureUrl(eventRequestDto.getPictureUrl());
        event.setName(eventRequestDto.getName());
        event.setDescription(eventRequestDto.getDescription());
        event.setStartingDate(Instant.parse(eventRequestDto.getStartingDate()));
        event.setEndingDate(Instant.parse(eventRequestDto.getEndingDate()));
        event.setSpotsAmount(eventRequestDto.getSpotsAmount());

        return event;
    }
}
