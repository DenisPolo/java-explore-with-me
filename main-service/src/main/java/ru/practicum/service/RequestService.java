package ru.practicum.service;

import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(long userId, long eventId);

    List<ParticipationRequestDto> getRequestsByEventInitiator(long userId, long eventId);

    List<ParticipationRequestDto> getRequestsByEventsParticipant(long userId);

    EventRequestStatusUpdateResult updateRequestsByEventInitiator(long userId, long eventId,
                                                                  EventRequestStatusUpdateRequest updateRequest);

    ParticipationRequestDto cancelRequestByEventsParticipant(long userId, long requestId);
}