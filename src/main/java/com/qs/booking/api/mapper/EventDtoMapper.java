package com.qs.booking.api.mapper;

import com.qs.booking.api.dto.external.request.post.EventPostDto;
import com.qs.booking.api.dto.external.response.EventResponseDto;
import com.qs.booking.store.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    public Event toEntity(EventPostDto eventPostDto) {

        Event event = new Event();
        event.setPictureUrl(eventPostDto.getPictureUrl());
        event.setName(eventPostDto.getName());
        event.setDescription(eventPostDto.getDescription());
        event.setStartingDate(eventPostDto.getStartingDate());
        event.setEndingDate(eventPostDto.getEndingDate());
        event.setSpotsAmount(eventPostDto.getSpotsAmount());

        return event;
    }
}
