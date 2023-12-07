package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.constant.EventState;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class EventFullDto {

    @NotBlank(message = "'annotation' field must not be empty")
    private String annotation;

    @NotBlank(message = "'categoryDto' field must not be empty")
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;

    @NotBlank(message = "'eventDate' field must not be empty")
    private String eventDate;
    private Long id;

    @NotNull(message = "'initiator' must not be null")
    private UserShortDto initiator;

    @NotNull(message = "'location' must not be null")
    private LocationDto location;

    @NotNull(message = "'paid' must not be null")
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;

    @NotBlank(message = "'title' field must not be empty")
    private String title;
    private Long views;

    @Override
    public String toString() {
        return "EventFullDto"
                + "{\"annotation\": \"" + annotation + "\","
                + "\"categoryDto\": " + category + ","
                + "\"confirmedRequests\": " + confirmedRequests + "\","
                + "\"createdOn\": \"" + createdOn + "\","
                + "\"eventDate\": \"" + eventDate + "\","
                + "\"description\": \"" + description + "\","
                + "\"id\": " + id + ","
                + "\"initiator\": " + initiator + ","
                + "\"location\": " + location + ","
                + "\"paid\": " + paid + ","
                + "\"participantLimit\": " + participantLimit + ","
                + "\"publishedOn\": \"" + publishedOn + "\","
                + "\"requestModeration\": " + requestModeration + ","
                + "\"state\": " + state + ","
                + "\"title\": \"" + title + "\","
                + "\"views\": " + views
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventFullDto eventFullDto = (EventFullDto) o;
        return annotation.equals(eventFullDto.annotation)
                && category.equals(eventFullDto.category)
                && confirmedRequests.equals(eventFullDto.confirmedRequests)
                && createdOn.equals(eventFullDto.createdOn)
                && description.equals(eventFullDto.description)
                && eventDate.equals(eventFullDto.eventDate)
                && id.equals(eventFullDto.id)
                && initiator.equals(eventFullDto.initiator)
                && location.equals(eventFullDto.location)
                && paid.equals(eventFullDto.paid)
                && participantLimit.equals(eventFullDto.participantLimit)
                && publishedOn.equals(eventFullDto.publishedOn)
                && requestModeration.equals(eventFullDto.requestModeration)
                && state.equals(eventFullDto.state)
                && title.equals(eventFullDto.title)
                && views.equals(eventFullDto.views);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (annotation == null) ? 0 : annotation.hashCode();
        result += (category == null) ? 0 : category.hashCode();
        result += (confirmedRequests == null) ? 0 : confirmedRequests.hashCode();
        result += (createdOn == null) ? 0 : createdOn.hashCode();
        result += (description == null) ? 0 : description.hashCode();
        result += (eventDate == null) ? 0 : eventDate.hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (initiator == null) ? 0 : initiator.hashCode();
        result += (location == null) ? 0 : location.hashCode();
        result += (paid == null) ? 0 : paid.hashCode();
        result += (participantLimit == null) ? 0 : participantLimit.hashCode();
        result += (publishedOn == null) ? 0 : publishedOn.hashCode();
        result += (requestModeration == null) ? 0 : requestModeration.hashCode();
        result += (state == null) ? 0 : state.hashCode();
        result += (title == null) ? 0 : title.hashCode();
        result += (views == null) ? 0 : views.hashCode();
        return result;
    }
}