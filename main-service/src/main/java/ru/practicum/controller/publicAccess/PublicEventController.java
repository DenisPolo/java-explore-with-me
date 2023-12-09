package ru.practicum.controller.publicAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EWMStatClient;
import ru.practicum.constant.Sort;
import ru.practicum.controller.queryParams.QueryPublicParams;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.log.Log;
import ru.practicum.service.EventService;
import ru.practicum.statDto.StatHitDto;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.Constants.*;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService service;
    private final EWMStatClient statClient;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") Sort sort,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size,
            HttpServletRequest request) {
        Log.setRequestLog("public:", request);

        QueryPublicParams params = new QueryPublicParams(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);

        ResponseEntity<List<EventShortDto>> response = ResponseEntity.ok().body(service.getEvents(params, from, size));

        statClient.postHit(new StatHitDto(APP, request.getRequestURI(), request.getRemoteAddr(),
                CURRENT_TIME.format(DATE_TIME_FORMATTER)));

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable Long id,
            HttpServletRequest request) throws MalformedURLException, URISyntaxException {
        Log.setRequestLog("public:", request);

        StatHitDto statHitDto = new StatHitDto(APP, request.getRequestURI(), request.getRemoteAddr(),
                CURRENT_TIME.format(DATE_TIME_FORMATTER));

        boolean uniqueIp = statClient.checkUniqueIp(statHitDto);

        ResponseEntity<EventFullDto> response = ResponseEntity.ok().body(service.getEvent(id, uniqueIp));

        statClient.postHit(statHitDto);

        return response;
    }
}