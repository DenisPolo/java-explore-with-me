package ru.practicum.controller.privateAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.log.Log;
import ru.practicum.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class UsersRequestsController {
    private final RequestService service;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable long userId,
                                                                 @RequestParam long eventId,
                                                                 HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(service.createRequest(userId, eventId));
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable long userId,
                                                                     HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.ok().body(service.getRequestsByEventsParticipant(userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable long userId,
                                                                 @PathVariable long requestId,
                                                                 HttpServletRequest request) {
        Log.setRequestLog("user:", request);
        return ResponseEntity.ok().body(service.cancelRequestByEventsParticipant(userId, requestId));
    }
}