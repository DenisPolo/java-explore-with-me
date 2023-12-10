package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.location.MainLocationDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.MainLocationMapper;
import ru.practicum.model.MainLocation;
import ru.practicum.repository.MainLocationRepository;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.MainLocationService;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MainLocationServiceImpl implements MainLocationService {
    private final MainLocationRepository repository;

    @Override
    public MainLocationDto createLocation(MainLocationDto newLocationDto) {
        MainLocation mainLocation = MainLocationMapper.INSTANCE.mapToNewMainLocation(newLocationDto);
        return MainLocationMapper.INSTANCE.mapToMainLocationDto(repository.save(mainLocation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MainLocationDto> getLocations(List<Long> locationsIds, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size > 0 ? size : 10);

        if (locationsIds == null) {
            return MainLocationMapper.INSTANCE.mapToMainLocationDto(repository.findAll(page).toList());
        } else {
            return MainLocationMapper.INSTANCE.mapToMainLocationDto(repository
                    .findMainLocationByIdInOrderByNameAsc(locationsIds, page));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MainLocationDto getLocation(Long locationId) {
        return MainLocationMapper.INSTANCE.mapToMainLocationDto(repository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location with id: " + locationId + " doesn't exist")));
    }

    @Override
    public MainLocationDto updateLocation(MainLocationDto updateLocationDto) {
        if (updateLocationDto.getId() == null) {
            throw new BadRequestException("Location id is not present");
        }

        MainLocation mainLocation = repository.findById(updateLocationDto.getId())
                .orElseThrow(() -> new NotFoundException("Location with id: "
                        + updateLocationDto.getId() + " doesn't exist"));

        if (updateLocationDto.getName() != null) mainLocation.setName(updateLocationDto.getName());
        if (updateLocationDto.getDescription() != null) mainLocation.setDescription(updateLocationDto.getDescription());
        if (updateLocationDto.getLat() != null) mainLocation.setLat(updateLocationDto.getLat());
        if (updateLocationDto.getLon() != null) mainLocation.setLon(updateLocationDto.getLon());
        if (updateLocationDto.getRad() != null) mainLocation.setRad(updateLocationDto.getRad());

        return MainLocationMapper.INSTANCE.mapToMainLocationDto(repository.save(mainLocation));
    }

    @Override
    public ResponseFormat deleteLocation(long locationId) {
        if (repository.existsById(locationId)) {
            repository.deleteById(locationId);
        } else {
            throw new BadRequestException("Location with ID: " + locationId + " doesn't exist");
        }

        if (repository.existsById(locationId)) {
            String message = "Unknown error. Location with ID: " + locationId + " was not deleted";
            log.warn(message);
            throw new BadRequestException(message);
        } else {
            String message = "Location with ID: " + locationId + " successfully deleted";
            log.info(message);
            return new ResponseFormat(message, HttpStatus.OK);
        }
    }
}