package ru.practicum.service;

import ru.practicum.dto.location.MainLocationDto;
import ru.practicum.responseFormat.ResponseFormat;

import java.util.List;

public interface MainLocationService {

    MainLocationDto createLocation(MainLocationDto newLocationDto);

    List<MainLocationDto> getLocations(List<Long> locationsIds, Integer from, Integer size);

    MainLocationDto getLocation(Long locationId);

    MainLocationDto updateLocation(MainLocationDto updateLocationDto);

    ResponseFormat deleteLocation(long locationId);
}