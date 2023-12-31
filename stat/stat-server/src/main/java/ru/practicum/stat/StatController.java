package ru.practicum.stat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.queryParams.QueryParams;
import ru.practicum.statDto.StatHitDto;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    public ResponseEntity<StatHitDto> postHit(@RequestBody StatHitDto statHitDto, HttpServletRequest request) {
        log.info(String.format("%-11s", "request:") + String.format("%-10s", "(" + request.getMethod() + ")")
                + request.getRequestURL());
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(statService.postHit(statHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStat(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false", required = false) Boolean unique,
            HttpServletRequest request) {
        log.info(String.format("%-11s", "request:") + String.format("%-10s", "(" + request.getMethod() + ")")
                + request.getRequestURL() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()));
        QueryParams params = new QueryParams(start, end, uris, unique);
        return ResponseEntity.ok().body(statService.getStat(params));
    }

    @GetMapping("/checkUniqueIp")
    public ResponseEntity<Boolean> getIsUniqueIp(
            @RequestParam(name = "app") String app,
            @RequestParam(name = "uri") String uri,
            @RequestParam(name = "ip") String ip,
            HttpServletRequest request) {
        log.info(String.format("%-11s", "request:") + String.format("%-10s", "(" + request.getMethod() + ")")
                + request.getRequestURL() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()));
        StatHitDto statHitDto = new StatHitDto(app, uri, ip, null);
        return ResponseEntity.ok().body(statService.getIsUniqueIp(statHitDto));
    }
}