package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.constant.Constants;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.Request;

import java.util.List;

@Mapper(imports = Constants.class)
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "created", expression = "java(Constants.DATE_TIME_FORMATTER.format(request.getCreated()))")
    @Mapping(target = "event", expression = "java(request.getEvent().getId())")
    @Mapping(target = "requester", expression = "java(request.getRequester().getId())")
    ParticipationRequestDto mapToParticipationRequestDto(Request request);

    List<ParticipationRequestDto> mapToParticipationRequestDto(List<Request> requests);
}