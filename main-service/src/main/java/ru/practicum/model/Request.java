package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.constant.RequestState;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class Request {

    @Column(name = "created", nullable = false)
    private Instant created = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    private RequestState status;

    public Request(Event event, User requester, RequestState status) {
        this.event = event;
        this.requester = requester;
        this.status = status;
    }

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
        Request request = (Request) o;
        return created.equals(request.created)
                && event.equals(request.event)
                && id.equals(request.id)
                && requester.equals(request.requester)
                && status.equals(request.status);
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
