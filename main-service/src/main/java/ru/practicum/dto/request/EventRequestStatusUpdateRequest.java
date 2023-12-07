package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.constant.EventRequestStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private EventRequestStatus status;

    @Override
    public String toString() {
        return "EventRequestStatusUpdateRequest"
                + "{"
                + "\"requestIds\": " + requestIds + ","
                + "\"status\": " + status
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest = (EventRequestStatusUpdateRequest) o;
        return requestIds.equals(eventRequestStatusUpdateRequest.requestIds)
                && status.equals(eventRequestStatusUpdateRequest.status);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (requestIds == null) ? 0 : requestIds.hashCode();
        result += (status == null) ? 0 : status.hashCode();
        return result;
    }
}