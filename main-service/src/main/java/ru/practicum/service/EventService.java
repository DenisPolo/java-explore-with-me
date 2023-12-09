package ru.practicum.service;

import ru.practicum.controller.queryParams.QueryAdminParams;
import ru.practicum.controller.queryParams.QueryPublicParams;
import ru.practicum.dto.event.*;
import ru.practicum.responseFormat.ResponseFormat;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, int from, int size);

    List<EventFullDto> getEvents(QueryAdminParams params, int from, int size);

    List<EventShortDto> getEvents(QueryPublicParams params, int from, int size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto getEvent(Long eventId, boolean uniqueIp);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent);

    ResponseFormat deleteEvent(Long userId, Long eventId);
}