package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.constant.EventState;
import ru.practicum.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long userId, PageRequest page);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "JOIN e.location AS l " +
            "WHERE distance(l.lat, l.lon, ?1, ?2) < ?3 " +
            "AND e.state IN ?4 " +
            "ORDER BY e.id ASC")
    List<Event> findEventsInLocation(Float lat, Float lon, Float rad, List<EventState> states, PageRequest page);
}