package ru.practicum.controller.privateAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.log.Log;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class InitiatorEventsController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(
            @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEventDto,
            HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.created(URI.create(request.getRequestURI()))
                .body(eventService.createEvent(userId, newEventDto));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size,
            HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.ok().body(eventService.getEvents(userId, from, size));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.ok().body(eventService.getEvent(userId, eventId));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.ok().body(requestService.getRequestsByEventInitiator(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEvent,
            HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.ok().body(eventService.updateEvent(userId, eventId, updateEvent));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequest(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest,
            HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.ok().body(requestService.updateRequestsByEventInitiator(userId, eventId,
                statusUpdateRequest));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ResponseFormat> deleteEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        ResponseFormat response = eventService.deleteEvent(userId, eventId);
        return ResponseEntity.noContent().header("X-Deleted-Event-Info", response.getMessage()).build();
    }
}