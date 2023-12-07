package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class EventShortDto {

    @NotBlank(message = "'annotation' field must not be empty")
    private String annotation;

    @NotBlank(message = "'categoryDto' field must not be empty")
    private CategoryDto category;
    private Long confirmedRequests;

    @NotBlank(message = "'eventDate' field must not be empty")
    private String eventDate;
    private Long id;

    @NotNull(message = "'initiator' must not be null")
    private UserShortDto initiator;

    @NotNull(message = "'paid' must not be null")
    private Boolean paid;

    @NotBlank(message = "'title' field must not be empty")
    private String title;
    private Long views;

    @Override
    public String toString() {
        return "EventShortDto"
                + "{\"annotation\": \"" + annotation + "\","
                + "\"categoryDto\": " + category + ","
                + "\"confirmedRequests\": " + confirmedRequests + "\","
                + "\"eventDate\": \"" + eventDate + "\","
                + "\"id\": " + id + ","
                + "\"initiator\": " + initiator + ","
                + "\"paid\": " + paid + ","
                + "\"title\": \"" + title + "\","
                + "\"views\": " + views
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventShortDto eventShortDto = (EventShortDto) o;
        return annotation.equals(eventShortDto.annotation)
                && category.equals(eventShortDto.category)
                && confirmedRequests.equals(eventShortDto.confirmedRequests)
                && eventDate.equals(eventShortDto.eventDate)
                && id.equals(eventShortDto.id)
                && initiator.equals(eventShortDto.initiator)
                && paid.equals(eventShortDto.paid)
                && title.equals(eventShortDto.title)
                && views.equals(eventShortDto.views);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (annotation == null) ? 0 : annotation.hashCode();
        result += (category == null) ? 0 : category.hashCode();
        result += (confirmedRequests == null) ? 0 : confirmedRequests.hashCode();
        result += (eventDate == null) ? 0 : eventDate.hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (initiator == null) ? 0 : initiator.hashCode();
        result += (paid == null) ? 0 : paid.hashCode();
        result += (title == null) ? 0 : title.hashCode();
        result += (views == null) ? 0 : views.hashCode();
        return result;
    }
}