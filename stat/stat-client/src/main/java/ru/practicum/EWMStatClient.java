package ru.practicum;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statDto.StatDto;
import ru.practicum.statDto.StatHitDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EWMStatClient {
    private final RestTemplate rest = new RestTemplate();
    private final String path = "http://localhost:9090";

    public boolean postHit(StatHitDto statHitDto) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(statHitDto);

        StatHitDto response = rest.postForObject(URI.create(path + "/hit"), requestEntity, StatHitDto.class);

        System.out.println(response);

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

        StatDto[] statDtos = rest.getForObject(makePath(path, uris, unique), StatDto[].class, parameters);

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