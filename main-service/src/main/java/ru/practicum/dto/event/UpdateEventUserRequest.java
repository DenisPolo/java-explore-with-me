package ru.practicum.dto.event;

import lombok.Getter;
import ru.practicum.constant.StateAction;
import ru.practicum.dto.location.LocationDto;

import javax.validation.constraints.Size;

@Getter
public class UpdateEventUserRequest extends UpdateEventRequest {
    public UpdateEventUserRequest(@Size(min = 20, message = "Annotation length must not be less than 20")
                                  @Size(max = 2000, message = "Annotation length must not be more than 2000") String annotation,
                                  Long category,
                                  @Size(min = 20, message = "Description length must not be less than 20")
                                  @Size(max = 7000, message = "Description length must not be more than 7000") String description,
                                  String eventDate,
                                  LocationDto location,
                                  Boolean paid,
                                  Integer participantLimit,
                                  Boolean requestModeration,
                                  StateAction stateAction,
                                  @Size(min = 3, message = "Title length must not be less than 3")
                                  @Size(max = 120, message = "Title length must not be more than 120") String title) {
        super(annotation, category, description, eventDate, location, paid, participantLimit, requestModeration, stateAction, title);
    }
}