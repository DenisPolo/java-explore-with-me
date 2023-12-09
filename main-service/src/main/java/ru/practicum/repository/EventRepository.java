package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long userId, PageRequest page);

    List<Event> findDistinctItemByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String annotationText,
            String descriptionText2);
}