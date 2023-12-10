package ru.practicum.controller.queryParams;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coordinates {
    private Float lat;
    private Float lon;
    private Float rad;
}