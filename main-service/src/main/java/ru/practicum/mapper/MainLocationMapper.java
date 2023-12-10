package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.location.MainLocationDto;
import ru.practicum.model.MainLocation;

import java.util.List;

@Mapper
public interface MainLocationMapper {

    MainLocationMapper INSTANCE = Mappers.getMapper(MainLocationMapper.class);

    MainLocationDto mapToMainLocationDto(MainLocation mainLocation);

    List<MainLocationDto> mapToMainLocationDto(List<MainLocation> mainLocations);

    @Mapping(target = "id", expression = "java(null)")
    MainLocation mapToNewMainLocation(MainLocationDto mainLocationDto);
}