package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "events_compilations",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Set<Event> events = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created")
    private Instant created = Instant.now();

    public Compilation(Set<Event> events, Boolean pinned, String title) {
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Compilation"
                + "{\"events\": " + events + ","
                + "\"id\": " + id + ","
                + "\"pinned\": " + pinned + ","
                + "\"title\": \"" + title + "\","
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation compilation = (Compilation) o;
        return events.equals(compilation.events)
                && id.equals(compilation.id)
                && pinned.equals(compilation.pinned)
                && title.equals(compilation.title);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (events == null) ? 0 : events.hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (pinned == null) ? 0 : pinned.hashCode();
        result += (title == null) ? 0 : title.hashCode();
        return result;
    }
}