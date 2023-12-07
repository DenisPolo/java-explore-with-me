package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statDto.StatDto;
import ru.practicum.statDto.StatHitDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EWMStatClient {
    private final RestTemplate rest;

    @Autowired
    public EWMStatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public boolean postHit(StatHitDto statHitDto) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(statHitDto);

        StatHitDto response = rest.exchange("/hit", HttpMethod.POST, requestEntity, StatHitDto.class).getBody();

        return (Objects.requireNonNull(response).getId() != null && response.getUri().equals(statHitDto.getUri()));
    }

    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return getResponse(start, end, uris, unique);
    }

    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris) {
        return getResponse(start, end, uris, null);
    }

    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, Boolean unique) {
        return getResponse(start, end, null, unique);
    }

    public List<StatDto> getStat(LocalDateTime start, LocalDateTime end) {
        return getResponse(start, end, null, null);
    }

    public boolean checkUniqueIp(StatHitDto statHitDto) {
        String path = "/checkUniqueIp?app=" + statHitDto.getApp()
                + "&uri=" + statHitDto.getUri() + "&ip=" + statHitDto.getIp();

        ResponseEntity<Boolean> response = rest.exchange(path, HttpMethod.GET, null, Boolean.class);

        return Boolean.TRUE.equals(response.getBody());
    }

    private List<StatDto> getResponse(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String startTime;
        String endTime;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

        try {
            startTime = Objects.requireNonNull(start).format(formatter);
            endTime = Objects.requireNonNull(end).format(formatter);
        } catch (NullPointerException e) {
            throw new RuntimeException("'start' and 'end' fields must not be null");
        }

        Map<String, Object> parameters = new java.util.HashMap<>(Map.of(
                "start", startTime,
                "end", endTime
        ));

        StatDto[] statDtos = rest.exchange(makePath("", uris, unique), HttpMethod.GET, null, StatDto[].class,
                parameters).getBody();

        return Arrays.asList(Objects.requireNonNull(statDtos));
    }

    private String makePath(String path, List<String> uris, Boolean unique) {
        StringBuilder requestPath = new StringBuilder(path);

        requestPath.append("/stats?start={start}&end={end}");

        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                requestPath.append("&uris=").append(uri);
            }
        }

        if (unique != null) {
            requestPath.append("&unique=").append(unique);
        }

        return requestPath.toString();
    }
}