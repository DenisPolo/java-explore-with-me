package ru.practicum.controller.queryParams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.constant.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class QueryAdminParams {
    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}