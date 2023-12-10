package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.MainLocation;

import java.util.List;

@Repository
public interface MainLocationRepository extends JpaRepository<MainLocation, Long> {
    List<MainLocation> findMainLocationByIdInOrderByNameAsc(List<Long> locationsIds, PageRequest page);
}