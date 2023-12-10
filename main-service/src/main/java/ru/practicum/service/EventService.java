package ru.practicum.service;

import ru.practicum.constant.EventState;
import ru.practicum.controller.queryParams.QueryAdminParams;
import ru.practicum.controller.queryParams.QueryPublicParams;
import ru.practicum.dto.event.*;
import ru.practicum.responseFormat.ResponseFormat;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, int from, int size);

    List<EventFullDto> getEventsFilterAdmin(QueryAdminParams params, int from, int size);

    List<EventShortDto> getEventsFilter(QueryPublicParams params, int from, int size);

    List<EventFullDto> getEventsInLocationByAdmin(Long locationId, List<EventState> states, int from, int size);

    List<EventFullDto> getEventsInCoordinatesByAdmin(Float lat, Float lon, Float rad, List<EventState> states, int from, int size);

    List<EventShortDto> getEventsInLocation(Long locationId, int from, int size);

    List<EventShortDto> getEventsInCoordinates(Float lat, Float lon, Float rad, int from, int size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto getEvent(Long eventId, boolean uniqueIp);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent);

    ResponseFormat deleteEvent(Long userId, Long eventId);
}