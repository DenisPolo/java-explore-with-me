package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipationRequestDto {
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private String status;

    @Override
    public String toString() {
        return "ParticipationRequestDto"
                + "{\"created\": \"" + created + "\","
                + "{\"event\": " + event + ","
                + "{\"id\": " + id + ","
                + "{\"requester\": " + requester + ","
                + "\"status\": \"" + status + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationRequestDto participationRequestDto = (ParticipationRequestDto) o;
        return created.equals(participationRequestDto.created)
                && event.equals(participationRequestDto.event)
                && id.equals(participationRequestDto.id)
                && requester.equals(participationRequestDto.requester)
                && status.equals(participationRequestDto.status);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (created == null) ? 0 : created.hashCode();
        result += (event == null) ? 0 : event.hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (requester == null) ? 0 : requester.hashCode();
        result += (status == null) ? 0 : status.hashCode();
        return result;
    }
}