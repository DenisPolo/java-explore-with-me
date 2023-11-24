package ru.practicum.stat.queryParams;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class QueryParams {

    private LocalDateTime start;

    private LocalDateTime end;

    private List<String> uris;

    private Boolean unique;
}