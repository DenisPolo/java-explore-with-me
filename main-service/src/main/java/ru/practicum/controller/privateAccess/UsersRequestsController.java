package ru.practicum.controller.privateAccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class UsersRequestsController {
    private final RequestService service;

    @PostMapping
    @Transactional
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable long userId,
                                                                 @RequestParam long eventId,
                                                                 HttpServletRequest request) {
        String requestParams = request.getQueryString();
        log.info("user: " + "(" + request.getMethod() + ")" + request.getRequestURL()
                + (requestParams == null ? "" : "?" + requestParams));
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(service.createRequest(userId, eventId));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable long userId,
                                                                     HttpServletRequest request) {
        log.info("user: " + "(" + request.getMethod() + ")" + request.getRequestURL());
        return ResponseEntity.ok().body(service.getRequestsByEventsParticipant(userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable long userId,
                                                                 @PathVariable long requestId,
                                                                 HttpServletRequest request) {
        log.info("user: " + "(" + request.getMethod() + ")" + request.getRequestURL());
        return ResponseEntity.ok().body(service.cancelRequestByEventsParticipant(userId, requestId));
    }
}