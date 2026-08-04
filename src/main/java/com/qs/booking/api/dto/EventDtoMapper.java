package com.qs.booking.api.dto;

import com.qs.booking.api.mapper.AccountDtoMapper;
import com.qs.booking.store.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EventDtoMapper {

    @Autowired
    private AccountDtoMapper accountDtoMapper;

    public EventResponseDto toDto(Event event) {

        return EventResponseDto
                .builder()
                .id(event.getId().toString())
                .pictureUrl(event.getPictureUrl())
                .name(event.getName())
                .description(event.getDescription())
                .startingDate(event.getStartingDate().toString())
                .endingDate(event.getEndingDate().toString())
                .author(accountDtoMapper.toDto(event.getAuthor()))
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
