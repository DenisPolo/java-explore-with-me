package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constant.Constants;
import ru.practicum.constant.EventState;
import ru.practicum.constant.Sort;
import ru.practicum.constant.StateAction;
import ru.practicum.controller.queryParams.Coordinates;
import ru.practicum.controller.queryParams.QueryAdminParams;
import ru.practicum.controller.queryParams.QueryPublicParams;
import ru.practicum.dto.event.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.*;
import ru.practicum.repository.*;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.EventService;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.constant.Constants.CURRENT_TIME;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MainLocationRepository mainLocationRepository;
    private final SessionFactory sessionFactory;

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        if (CURRENT_TIME.isAfter(LocalDateTime.parse(newEventDto.getEventDate(), Constants.DATE_TIME_FORMATTER))) {
            throw new BadRequestException("Event date can't be in the past");
        }

        if (CURRENT_TIME.plusHours(2).isAfter(LocalDateTime.parse(newEventDto.getEventDate(),
                Constants.DATE_TIME_FORMATTER))) {
            throw new BadRequestException("Event date can't be in the past");
        }

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id: " + newEventDto.getCategory()
                        + " doesn't exist"));

        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " doesn't exist"));

        Location location = locationRepository.save(LocationMapper.INSTANCE.mapToNewLocation(newEventDto.getLocation()));

        Event event = EventMapper.INSTANCE.mapToNewEvent(category, initiator, location, newEventDto);

        return EventMapper.INSTANCE.mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id: " + userId + " doesn't exist");
        }

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size > 0 ? size : 10);

        return EventMapper.INSTANCE.mapToEventShortDto(eventRepository.findAllByInitiatorId(userId, page));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsFilterAdmin(QueryAdminParams params, int from, int size) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);
        event.fetch("category");
        event.fetch("initiator");
        event.fetch("location");

        List<Predicate> predicates = new ArrayList<>();

        if (params.getUsers() != null) {
            predicates.add(cb.and(event.get("initiator").in(params.getUsers())));
        }

        setLocationsPredicates(cb, event, predicates, params.getMainLocations(), params.getCoordinates());

        if (params.getStates() != null) {
            predicates.add(cb.and(event.get("state").in(params.getStates())));
        }

        if (params.getCategories() != null) {
            predicates.add(cb.and(event.get("category").in(params.getCategories())));
        }

        if (params.getRangeStart() != null && params.getRangeEnd() != null) {
            if (params.getRangeStart().isAfter(params.getRangeEnd())) {
                throw new BadRequestException("RangeEnd is before RangeStart");
            }

            predicates.add(cb.between(event.get("eventDate"), params.getRangeStart(), params.getRangeEnd()));
        } else if (params.getRangeStart() != null) {
            predicates.add(cb.greaterThan(event.get("eventDate"), params.getRangeStart()));
        } else if (params.getRangeEnd() != null) {
            predicates.add(cb.lessThan(event.get("eventDate"), params.getRangeEnd()));
        }

        cq.where(predicates.toArray(new Predicate[0])).orderBy(cb.asc(event.get("id")));

        Query<Event> query = session.createQuery(cq);
        query.setFirstResult(Math.toIntExact(from));
        query.setMaxResults(size);
        List<Event> results = query.getResultList();
        session.close();

        return EventMapper.INSTANCE.mapToEventFullDto(results);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<EventShortDto> getEventsFilter(QueryPublicParams params, int from, int size) {
        Session session = sessionFactory.openSession().getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        predicates.add(cb.and(event.get("state").in(EventState.PUBLISHED)));

        if (params.getText() != null) {
            predicates.add(cb.or((cb.like(cb.lower(event.get("annotation")),
                            ("%" + params.getText() + "%").toLowerCase())),
                    (cb.like(cb.lower(event.get("description")),
                            ("%" + params.getText() + "%").toLowerCase()))));
        }

        setLocationsPredicates(cb, event, predicates, params.getMainLocations(), params.getCoordinates());

        if (params.getCategories() != null) {
            predicates.add(cb.and(event.get("category").in(params.getCategories())));
        }

        if (params.getPaid() != null) {
            predicates.add(cb.equal(event.get("paid"), params.getPaid()));
        }

        if (params.getRangeStart() != null && params.getRangeEnd() != null) {
            if (params.getRangeStart().isAfter(params.getRangeEnd())) {
                throw new BadRequestException("RangeEnd is before RangeStart");
            }

            predicates.add(cb.between(event.get("eventDate"), params.getRangeStart(), params.getRangeEnd()));
        } else if (params.getRangeStart() != null
                && params.getRangeStart().isAfter(CURRENT_TIME)) {
            predicates.add(cb.greaterThan(event.get("eventDate"), params.getRangeStart()));
        } else if (params.getRangeEnd() != null
                && params.getRangeStart().isAfter(CURRENT_TIME.plusHours(1))) {
            predicates.add(cb.between(event.get("eventDate"), CURRENT_TIME, params.getRangeEnd()));
        } else {
            predicates.add(cb.greaterThan(event.get("eventDate"), CURRENT_TIME));
        }

        if (params.getOnlyAvailable() != null && params.getOnlyAvailable()) {
            predicates.add(cb.or(cb.lessThan(event.get("confirmedRequests"), event.get("participantLimit")),
                    cb.equal(event.get("participantLimit"), 0)));
        }

        if (params.getSort().equals(Sort.EVENT_DATE)) {
            orderList.add(cb.desc(event.get("eventDate")));
        } else if (params.getSort().equals(Sort.VIEWS)) {
            orderList.add(cb.desc(event.get("views")));
        } else {
            throw new BadRequestException("Unknown sort parameter");
        }

        event.fetch("category");
        event.fetch("initiator");
        event.fetch("location");

        cq.where(predicates.toArray(new Predicate[0])).orderBy(orderList);

        Query<Event> query = session.createQuery(cq);
        query.setFirstResult(Math.toIntExact(from));
        query.setMaxResults(size);
        List<Event> results = query.getResultList();
        session.close();

        return EventMapper.INSTANCE.mapToEventShortDto(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsInLocationByAdmin(Long mainLocationId, List<EventState> states,
                                                         int from, int size) {
        if (states == null) states = List.of(EventState.PENDING, EventState.PUBLISHED, EventState.CANCELED);
        return EventMapper.INSTANCE.mapToEventFullDto(findEventsInLocation(mainLocationId, states, from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsInCoordinatesByAdmin(Float lat, Float lon, Float rad, List<EventState> states,
                                                            int from, int size) {
        if (states == null) states = List.of(EventState.PENDING, EventState.PUBLISHED, EventState.CANCELED);
        return EventMapper.INSTANCE.mapToEventFullDto(findEventsInCoordinates(lat, lon, rad, states, from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsInLocation(Long mainLocationId, int from, int size) {
        return EventMapper.INSTANCE.mapToEventShortDto(findEventsInLocation(mainLocationId,
                List.of(EventState.PUBLISHED), from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsInCoordinates(Float lat, Float lon, Float rad, int from, int size) {
        return EventMapper.INSTANCE.mapToEventShortDto(findEventsInCoordinates(lat, lon, rad,
                List.of(EventState.PUBLISHED), from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEvent(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id: " + userId + " doesn't exist");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id: "
                + eventId + " doesn't exist"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("User with id: " + userId + " isn't initiator of event with id: " + eventId);
        }

        return EventMapper.INSTANCE.mapToEventFullDto(event);
    }

    @Override
    public EventFullDto getEvent(Long eventId, boolean uniqueIp) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id: "
                + eventId + " doesn't exist"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event with id: " + eventId + " not published");
        }

        if (uniqueIp) event.setViews(event.getViews() == null ? 1 : event.getViews() + 1);

        eventRepository.save(event);

        return EventMapper.INSTANCE.mapToEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id: " + userId + " doesn't exist");
        }

        Event updatableEvent = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " doesn't exist");
        });

        if (!updatableEvent.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("User with id: " + userId + " isn't initiator of event with id: " + eventId);
        }

        if (updatableEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event with id: " + eventId + " must be not published");
        }

        setUpdatableFields(updatableEvent, updateEvent);

        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case SEND_TO_REVIEW:
                    updatableEvent.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    updatableEvent.setState(EventState.CANCELED);
                    break;
                default:
                    throw new BadRequestException("'stateAction' field got incorrect value");
            }
        }

        return EventMapper.INSTANCE.mapToEventFullDto(eventRepository.save(updatableEvent));
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event updatableEvent = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " doesn't exist");
        });

        if (updatableEvent.getState().equals(EventState.CANCELED) && updateEvent.getStateAction() != null) {
            throw new ConflictException("Event with id: " + eventId + " must be not canceled, you can't "
                    + (updateEvent.getStateAction().equals(StateAction.PUBLISH_EVENT) ? "publish" : "reject")
                    + " not pending event");
        }

        if (updatableEvent.getState().equals(EventState.PUBLISHED) && updateEvent.getStateAction() != null) {
            throw new ConflictException("Event with id: " + eventId + " already published");
        }

        if (CURRENT_TIME.plusHours(1).isAfter(updatableEvent.getEventDate())) {
            throw new ForbiddenException("Event with id: " + eventId + " will begin less then 1 hour");
        }

        setUpdatableFields(updatableEvent, updateEvent);

        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case PUBLISH_EVENT:
                    updatableEvent.setState(EventState.PUBLISHED);
                    updatableEvent.setPublishedOn(CURRENT_TIME);
                    break;
                case REJECT_EVENT:
                    updatableEvent.setState(EventState.CANCELED);
                    break;
                default:
                    throw new BadRequestException("'stateAction' field got incorrect value");
            }
        }

        return EventMapper.INSTANCE.mapToEventFullDto(eventRepository.save(updatableEvent));
    }

    @Override
    public ResponseFormat deleteEvent(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id: " + userId + " doesn't exist");
        }

        Event deletedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " doesn't exist"));

        if (!deletedEvent.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("User with id: " + userId + " isn't initiator of event with id: " + eventId);
        }

        eventRepository.deleteById(eventId);

        if (eventRepository.existsById(eventId)) {
            throw new RuntimeException("Failed to delete event with ID: " + eventId);
        }

        locationRepository.deleteById(deletedEvent.getLocation().getId());

        if (locationRepository.existsById(deletedEvent.getLocation().getId())) {
            throw new RuntimeException("Failed to delete " + deletedEvent.getLocation());
        }

        String message = "Event with ID: " + userId + " successfully deleted";

        log.info(message);

        return new ResponseFormat(message, HttpStatus.OK);
    }

    private <T extends UpdateEventRequest> void setUpdatableFields(Event updatableEvent, T updateEvent) {
        if (updateEvent.getAnnotation() != null) updatableEvent.setAnnotation(updateEvent.getAnnotation());

        if (updateEvent.getCategory() != null) {
            updatableEvent.setCategory(categoryRepository.findById(updateEvent.getCategory()).orElseThrow(() -> {
                throw new NotFoundException("Category with id: " + updateEvent.getCategory() + " doesn't exist");
            }));
        }

        if (updateEvent.getDescription() != null) updatableEvent.setDescription(updateEvent.getDescription());

        if (updateEvent.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(updateEvent.getEventDate(), Constants.DATE_TIME_FORMATTER);
            if (CURRENT_TIME.isAfter(newEventDate)) {
                throw new BadRequestException("Event date can't be in the past");
            }

            if (CURRENT_TIME.plusHours(2).isAfter(newEventDate)) {
                throw new BadRequestException("Less than two hours until the start of the event");
            }

            updatableEvent.setEventDate(LocalDateTime.parse(updateEvent.getEventDate(), Constants.DATE_TIME_FORMATTER));
        }

        if (updateEvent.getLocation() != null) {
            Location updatableLocation = updatableEvent.getLocation();
            updatableLocation.setLon(updateEvent.getLocation().getLon());
            updatableLocation.setLat(updateEvent.getLocation().getLat());
            updatableEvent.setLocation(locationRepository.save(updatableLocation));
        }

        if (updateEvent.getPaid() != null) updatableEvent.setPaid(updateEvent.getPaid());
        if (updateEvent.getParticipantLimit() != null)
            updatableEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        if (updateEvent.getRequestModeration() != null)
            updatableEvent.setRequestModeration(updateEvent.getRequestModeration());
        if (updateEvent.getTitle() != null) updatableEvent.setTitle(updateEvent.getTitle());
    }

    private List<Event> findEventsInLocation(Long mainLocationId, List<EventState> states, int from, int size) {
        MainLocation mainLocation = mainLocationRepository.findById(mainLocationId)
                .orElseThrow(() -> new NotFoundException("Main location with ID: " + mainLocationId
                        + " doesn't exist"));

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size > 0 ? size : 10);

        return eventRepository.findEventsInLocation(mainLocation
                .getLat(), mainLocation.getLon(), mainLocation.getRad(), states, page);
    }

    private void setLocationsPredicates(
            CriteriaBuilder cb, Root<Event> event, List<Predicate> predicates,
            List<Long> mainLocationsIds, Coordinates coordinates) {
        if (mainLocationsIds != null && coordinates != null) {
            List<Predicate> locations = new ArrayList<>();
            List<MainLocation> mainLocations = mainLocationRepository.findAllById(mainLocationsIds);

            for (MainLocation mainLocation : mainLocations) {
                locations.add(cb.lessThan(
                        cb.function("distance", Float.class,
                                event.get("location").get("lat"),
                                event.get("location").get("lon"),
                                cb.literal(mainLocation.getLat()),
                                cb.literal(mainLocation.getLon())),
                        cb.literal(mainLocation.getRad()))
                );
            }

            locations.add(cb.lessThan(
                    cb.function("distance", Float.class, event.get("location").get("lat"),
                            event.get("location").get("lon"),
                            cb.literal(coordinates.getLat()),
                            cb.literal(coordinates.getLon())),
                    cb.literal(coordinates.getRad()))
            );

            predicates.add(cb.or(locations.toArray(new Predicate[0])));
        } else if (mainLocationsIds != null) {
            List<Predicate> locations = new ArrayList<>();
            List<MainLocation> mainLocations = mainLocationRepository.findAllById(mainLocationsIds);

            for (MainLocation mainLocation : mainLocations) {
                locations.add(cb.lessThan(
                        cb.function("distance", Float.class,
                                event.get("location").get("lat"),
                                event.get("location").get("lon"),
                                cb.literal(mainLocation.getLat()),
                                cb.literal(mainLocation.getLon())),
                        cb.literal(mainLocation.getRad()))
                );
            }

            predicates.add(cb.or(locations.toArray(new Predicate[0])));
        } else if (coordinates != null) {
            predicates.add(cb.lessThan(
                    cb.function("distance", Float.class, event.get("location").get("lat"),
                            event.get("location").get("lon"),
                            cb.literal(coordinates.getLat()),
                            cb.literal(coordinates.getLon())),
                    cb.literal(coordinates.getRad()))
            );
        }
    }

    private List<Event> findEventsInCoordinates(Float lat, Float lon, Float rad, List<EventState> states,
                                                int from, int size) {
        if (lat < -90 || lat > 90) {
            throw new BadRequestException("Latitude range must be in range from -90 to 90 (provided: " + lat + ")");
        } else if (lon < -180 || lon > 180) {
            throw new BadRequestException("Longitude range must be in range from -180 to 180 (provided: " + lon + ")");
        } else if (rad < 0 || rad > 100000) {
            throw new BadRequestException("Radius must be not greater than 100000 or less than 0 meters (provided: "
                    + rad + ")");
        }

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size > 0 ? size : 10);

        return eventRepository.findEventsInLocation(lat, lon, rad, states, page);
    }
}