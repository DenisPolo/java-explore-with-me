package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    @Override
    public String toString() {
        return "EventRequestStatusUpdateResult"
                + "{"
                + "\"confirmedRequests\": " + confirmedRequests + ","
                + "\"rejectedRequests\": " + rejectedRequests
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = (EventRequestStatusUpdateResult) o;
        return confirmedRequests.equals(eventRequestStatusUpdateResult.confirmedRequests)
                && rejectedRequests.equals(eventRequestStatusUpdateResult.rejectedRequests);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (confirmedRequests == null) ? 0 : confirmedRequests.hashCode();
        result += (rejectedRequests == null) ? 0 : rejectedRequests.hashCode();
        return result;
    }
}