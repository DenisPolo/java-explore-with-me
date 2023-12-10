package ru.practicum.controller.queryParams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.constant.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class QueryPublicParams {
    private String text;
    private List<Long> mainLocations;
    private Coordinates coordinates;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private Sort sort;
}