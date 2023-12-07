package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.constant.EventState;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn = Instant.now();

    @Column(name = "description")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "views")
    private Long views;

    @Override
    public String toString() {
        return "Event"
                + "{\"annotation\": \"" + annotation + "\","
                + "\"category\": " + category + ","
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
        Event event = (Event) o;
        return annotation.equals(event.annotation)
                && category.equals(event.category)
                && confirmedRequests.equals(event.confirmedRequests)
                && createdOn.equals(event.createdOn)
                && description.equals(event.description)
                && eventDate.equals(event.eventDate)
                && id.equals(event.id)
                && initiator.equals(event.initiator)
                && location.equals(event.location)
                && paid.equals(event.paid)
                && participantLimit.equals(event.participantLimit)
                && publishedOn.equals(event.publishedOn)
                && requestModeration.equals(event.requestModeration)
                && state.equals(event.state)
                && title.equals(event.title)
                && views.equals(event.views);
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