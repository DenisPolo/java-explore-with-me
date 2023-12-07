package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Location;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationDto mapToLocationDto(Location location);

    Location mapToNewLocation(LocationDto locationDto);
}