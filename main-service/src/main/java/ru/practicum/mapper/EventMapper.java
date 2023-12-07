package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.constant.Constants;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;

import java.util.List;

@Mapper(imports = Constants.class)
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "createdOn", expression = "java(Constants.DATE_TIME_FORMATTER.format(event.getCreatedOn()))")
    @Mapping(source = "event.eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "event.publishedOn", target = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "location", expression = "java(LocationMapper.INSTANCE.mapToLocationDto(event.getLocation()))")
    @Mapping(target = "category", expression = "java(CategoryMapper.INSTANCE.mapToCategoryDto(event.getCategory()))")
    EventFullDto mapToEventFullDto(Event event);

    @Mapping(source = "event.eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "category", expression = "java(CategoryMapper.INSTANCE.mapToCategoryDto(event.getCategory()))")
    EventShortDto mapToEventShortDto(Event event);

    List<EventFullDto> mapToEventFullDto(List<Event> events);

    List<EventShortDto> mapToEventShortDto(List<Event> events);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(source = "newEventDto.eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "state", expression = "java(EventState.PENDING)")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "newEventDto.paid", target = "paid", defaultValue = "false")
    @Mapping(source = "newEventDto.participantLimit", target = "participantLimit", defaultValue = "0")
    @Mapping(source = "newEventDto.requestModeration", target = "requestModeration", defaultValue = "true")
    @Mapping(target = "confirmedRequests", expression = "java(0)")
    Event mapToNewEvent(Category category, User initiator, Location location, NewEventDto newEventDto);
}