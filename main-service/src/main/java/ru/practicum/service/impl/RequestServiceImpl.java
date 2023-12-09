package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constant.EventRequestStatus;
import ru.practicum.constant.EventState;
import ru.practicum.constant.RequestState;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.RequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto createRequest(long requesterId, long eventId) {
        User requester = userRepository.findById(requesterId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + requesterId + " doesn't exist");
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " doesn't exist");
        });

        if (event.getInitiator().getId() == requesterId) {
            throw new ConflictException("Initiator can't create participation request for their own event");
        }

        if (requestRepository.existsRequestByRequesterIdAndEventId(requesterId, eventId)) {
            throw new ConflictException("User with id: " + requesterId + " already create request for event with id: "
                    + eventId);
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event must be published");
        }

        if (event.getParticipantLimit() != 0
                && event.getParticipantLimit().longValue() <= event.getConfirmedRequests()) {
            throw new ConflictException("Participation limit reached");
        }

        Request request;

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            int confirmedRequests = event.getConfirmedRequests();

            event.setConfirmedRequests(++confirmedRequests);

            request = new Request(event, requester, RequestState.CONFIRMED);

            eventRepository.save(event);
        } else {
            request = new Request(eventRepository.save(event), requester, RequestState.PENDING);
        }

        return RequestMapper.INSTANCE.mapToParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByEventInitiator(long initiatorId, long eventId) {
        if (!userRepository.existsById(initiatorId)) {
            throw new NotFoundException("User with id: " + initiatorId + " doesn't exist");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " doesn't exist");
        });

        if (event.getInitiator().getId() != initiatorId) {
            throw new ConflictException("User with id: " + initiatorId + " isn't initiator of event with id: "
                    + eventId);
        }

        List<Request> requests = requestRepository.findRequestsByInitiator(initiatorId, eventId, RequestState.PENDING);

        return RequestMapper.INSTANCE.mapToParticipationRequestDto(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByEventsParticipant(long requesterId) {
        if (!userRepository.existsById(requesterId)) {
            throw new NotFoundException("User with id: " + requesterId + " doesn't exist");
        }

        List<Request> requests = requestRepository.findRequestsByRequesterId(requesterId);

        return RequestMapper.INSTANCE.mapToParticipationRequestDto(requests);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestsByEventInitiator(
            long initiatorId, long eventId,
            EventRequestStatusUpdateRequest updateRequest) {
        List<Request> requests;
        List<ParticipationRequestDto> confirmedRequests;
        List<ParticipationRequestDto> rejectedRequests;

        if (!userRepository.existsById(initiatorId)) {
            throw new NotFoundException("User with id: " + initiatorId + " doesn't exist");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " doesn't exist");
        });

        if (event.getInitiator().getId() != initiatorId) {
            throw new ConflictException("User with id: " + initiatorId + " isn't initiator of event with id: " + eventId);
        }

        if (!event.getRequestModeration()) {
            throw new ConflictException("Event with id: " + eventId + " doesn't have request moderation");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ConflictException("Event confirmed requests already reached participant limit");
        }

        if (updateRequest.getRequestIds().isEmpty()) {
            throw new BadRequestException("User ids are not presented");
        }

        requests = requestRepository.findRequestsByInitiator(updateRequest.getRequestIds(), initiatorId, eventId);

        if (requests.isEmpty()) return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());

        if (updateRequest.getStatus().equals(EventRequestStatus.CONFIRMED)) {
            confirmedRequests = new ArrayList<>();
            rejectedRequests = new ArrayList<>();
            AtomicInteger count = new AtomicInteger(event.getConfirmedRequests());
            int limit = event.getParticipantLimit();

            requests.forEach(r -> {
                if (!r.getStatus().equals(RequestState.PENDING)) {
                    throw new ConflictException("Request with id: " + r.getId() + " doesn't have pending status");
                } else if (count.get() < limit) {
                    r.setStatus(RequestState.CONFIRMED);
                    confirmedRequests.add(RequestMapper.INSTANCE.mapToParticipationRequestDto(r));
                    count.getAndIncrement();
                } else {
                    r.setStatus(RequestState.REJECTED);
                    rejectedRequests.add(RequestMapper.INSTANCE.mapToParticipationRequestDto(r));
                }
            });

            event.setConfirmedRequests(count.get());

            eventRepository.save(event);
        } else if (updateRequest.getStatus().equals(EventRequestStatus.REJECTED)) {
            rejectedRequests = requests.stream()
                    .peek(r -> {
                        if (r.getStatus().equals(RequestState.PENDING)) {
                            r.setStatus(RequestState.REJECTED);
                        } else {
                            throw new ConflictException("Request with id: " + r.getId() + " doesn't have pending status");
                        }
                    })
                    .map(RequestMapper.INSTANCE::mapToParticipationRequestDto)
                    .collect(Collectors.toList());

            confirmedRequests = new ArrayList<>();
        } else {
            throw new BadRequestException("Unknown event request status");
        }

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public ParticipationRequestDto cancelRequestByEventsParticipant(long requesterId, long requestId) {
        if (!userRepository.existsById(requesterId)) {
            throw new NotFoundException("User with id: " + requesterId + " doesn't exist");
        }

        Request cancelledRequest = requestRepository.findById(requestId).orElseThrow(() -> {
            throw new NotFoundException("Request with id: " + requestId + " doesn't exist");
        });

        if (cancelledRequest.getStatus().equals(RequestState.CANCELED)) {
            throw new BadRequestException("Request with id: " + requestId + " already cancelled");
        }

        if (cancelledRequest.getStatus().equals(RequestState.CONFIRMED)) {
            Event event = cancelledRequest.getEvent();

            int confirmedRequests = event.getConfirmedRequests();

            event.setConfirmedRequests(--confirmedRequests);

            cancelledRequest.setEvent(event);
        }

        cancelledRequest.setStatus(RequestState.CANCELED);

        return RequestMapper.INSTANCE.mapToParticipationRequestDto(requestRepository.save(cancelledRequest));
    }
}