package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.dto.location.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class NewEventDto {

    @Size(min = 20, message = "Annotation length must not be less than 20")
    @Size(max = 2000, message = "Annotation length must not be more than 2000")
    @NotBlank(message = "'annotation' field must not be empty")
    private String annotation;

    @NotNull(message = "'categoryDto' must not be null")
    private Long category;

    @Size(min = 20, message = "Description length must not be less than 20")
    @Size(max = 7000, message = "Description length must not be more than 7000")
    @NotBlank(message = "'description' field must not be empty")
    private String description;

    @NotBlank(message = "'eventDate' field must not be empty")
    private String eventDate;

    @NotNull(message = "'location' must not be null")
    private LocationDto location;
    private Boolean paid;

    @PositiveOrZero(message = "'participantLimit' must not be negative")
    private Integer participantLimit;
    private Boolean requestModeration;

    @Size(min = 3, message = "Title length must not be less than 3")
    @Size(max = 120, message = "Title length must not be more than 120")
    @NotBlank(message = "'title' field must not be empty")
    private String title;

    @Override
    public String toString() {
        return "NewEventDto"
                + "{\"annotation\": \"" + annotation + "\","
                + "\"categoryDto\": " + category + ","
                + "\"eventDate\": \"" + eventDate + "\","
                + "\"description\": \"" + description + "\","
                + "\"location\": " + location + ","
                + "\"paid\": " + paid + ","
                + "\"participantLimit\": " + participantLimit + ","
                + "\"requestModeration\": " + requestModeration + ","
                + "\"title\": \"" + title + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewEventDto newEventDto = (NewEventDto) o;
        return annotation.equals(newEventDto.annotation)
                && category.equals(newEventDto.category)
                && description.equals(newEventDto.description)
                && eventDate.equals(newEventDto.eventDate)
                && location.equals(newEventDto.location)
                && paid.equals(newEventDto.paid)
                && participantLimit.equals(newEventDto.participantLimit)
                && requestModeration.equals(newEventDto.requestModeration)
                && title.equals(newEventDto.title);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (annotation == null) ? 0 : annotation.hashCode();
        result += (category == null) ? 0 : category.hashCode();
        result += (description == null) ? 0 : description.hashCode();
        result += (eventDate == null) ? 0 : eventDate.hashCode();
        result += (location == null) ? 0 : location.hashCode();
        result += (paid == null) ? 0 : paid.hashCode();
        result += (participantLimit == null) ? 0 : participantLimit.hashCode();
        result += (requestModeration == null) ? 0 : requestModeration.hashCode();
        result += (title == null) ? 0 : title.hashCode();
        return result;
    }
}