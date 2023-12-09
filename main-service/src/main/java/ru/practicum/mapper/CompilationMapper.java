package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.ArrayList;
import java.util.List;

@Mapper(imports = {ArrayList.class, Event.class})
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    @Mapping(target = "events",
            expression = "java(EventMapper.INSTANCE.mapToEventShortDto(new ArrayList<Event>(compilation.getEvents())))")
    CompilationDto mapToCompilationDto(Compilation compilation);

    List<CompilationDto> mapToCompilationDto(List<Compilation> compilations);
}