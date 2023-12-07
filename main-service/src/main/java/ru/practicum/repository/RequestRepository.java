package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.constant.RequestState;
import ru.practicum.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Boolean existsRequestByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findRequestsByRequesterId(long userId);

    @Query("SELECT r " +
            "FROM Request AS r " +
            "JOIN r.event AS e " +
            "JOIN e.initiator AS i " +
            "WHERE i.id = ?1 " +
            "AND e.id = ?2 " +
            "AND r.status = ?3 " +
            "ORDER BY r.id ASC")
    List<Request> findRequestsByInitiator(long initiatorId, long eventId, RequestState state);

    @Query("SELECT r " +
            "FROM Request AS r " +
            "JOIN r.event AS e " +
            "JOIN e.initiator AS i " +
            "WHERE r.id IN (?1) " +
            "AND i.id = ?2 " +
            "AND e.id = ?3 " +
            "ORDER BY r.id ASC")
    List<Request> findRequestsByInitiator(List<Long> requestIds, long initiatorId, long eventId);
}