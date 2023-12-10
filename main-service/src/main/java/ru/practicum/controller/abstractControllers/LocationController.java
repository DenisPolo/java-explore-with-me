package ru.practicum.controller.abstractControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.location.MainLocationDto;
import ru.practicum.log.Log;
import ru.practicum.service.EventService;
import ru.practicum.service.MainLocationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
public abstract class LocationController {
    protected final MainLocationService mainLocationService;
    protected final EventService eventService;
    protected String requester;

    @GetMapping
    public ResponseEntity<List<MainLocationDto>> getLocations(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size,
            HttpServletRequest request) {
        Log.setRequestLog(requester, request);
        return ResponseEntity.ok().body(mainLocationService.getLocations(ids, from, size));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<MainLocationDto> getLocation(
            @PathVariable Long locationId,
            HttpServletRequest request) {
        Log.setRequestLog(requester, request);
        return ResponseEntity.ok().body(mainLocationService.getLocation(locationId));
    }
}