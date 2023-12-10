package ru.practicum.controller.adminAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constant.EventState;
import ru.practicum.controller.queryParams.Coordinates;
import ru.practicum.controller.queryParams.QueryAdminParams;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.log.Log;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService service;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<Long> locations,
            @RequestParam(required = false) Float lat,
            @RequestParam(required = false) Float lon,
            @RequestParam(required = false) Float rad,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);

        Coordinates coordinates = null;

        if (lat != null && lon != null && rad != null) {
            if (lat > -90 && lat < 90 && lon > -180 && lon < 180 && rad > 0 && rad < 100000) {
                coordinates = new Coordinates(lat, lon, rad);
            }
        }

        QueryAdminParams params = new QueryAdminParams(users, locations, coordinates, states, categories, rangeStart, rangeEnd);
        return ResponseEntity.ok().body(service.getEventsFilterAdmin(params, from, size));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<EventFullDto>> getEventsInLocation(
            @PathVariable Long locationId,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.ok().body(service.getEventsInLocationByAdmin(locationId, states, from, size));
    }

    @GetMapping("/location")
    public ResponseEntity<List<EventFullDto>> getEventsInCoordinates(
            @RequestParam Float lat,
            @RequestParam Float lon,
            @RequestParam Float rad,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.ok().body(service.getEventsInCoordinatesByAdmin(lat, lon, rad, states, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateRequest,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.ok().body(service.updateEvent(eventId, updateRequest));
    }
}