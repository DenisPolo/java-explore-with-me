package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.constant.StateAction;
import ru.practicum.dto.location.LocationDto;

import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class UpdateEventRequest {

    @Size(min = 20, message = "Annotation length must not be less than 20")
    @Size(max = 2000, message = "Annotation length must not be more than 2000")
    private String annotation;
    private Long category;

    @Size(min = 20, message = "Description length must not be less than 20")
    @Size(max = 7000, message = "Description length must not be more than 7000")
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;

    @Size(min = 3, message = "Title length must not be less than 3")
    @Size(max = 120, message = "Title length must not be more than 120")
    private String title;

    @Override
    public String toString() {
        return "UpdateEventAdminRequest"
                + "{\"annotation\": \"" + annotation + "\","
                + "\"categoryDto\": " + category + ","
                + "\"eventDate\": \"" + eventDate + "\","
                + "\"description\": \"" + description + "\","
                + "\"location\": " + location + ","
                + "\"paid\": " + paid + ","
                + "\"participantLimit\": " + participantLimit + ","
                + "\"requestModeration\": " + requestModeration + ","
                + "\"state\": " + stateAction + ","
                + "\"title\": \"" + title + "\","
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateEventRequest updateEventRequest = (UpdateEventRequest) o;
        return annotation.equals(updateEventRequest.annotation)
                && category.equals(updateEventRequest.category)
                && description.equals(updateEventRequest.description)
                && eventDate.equals(updateEventRequest.eventDate)
                && location.equals(updateEventRequest.location)
                && paid.equals(updateEventRequest.paid)
                && participantLimit.equals(updateEventRequest.participantLimit)
                && requestModeration.equals(updateEventRequest.requestModeration)
                && stateAction.equals(updateEventRequest.stateAction)
                && title.equals(updateEventRequest.title);
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
        result += (stateAction == null) ? 0 : stateAction.hashCode();
        result += (title == null) ? 0 : title.hashCode();
        return result;
    }
}