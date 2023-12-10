package ru.practicum.controller.privateAccess;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.controller.abstractControllers.LocationController;
import ru.practicum.service.EventService;
import ru.practicum.service.MainLocationService;

@RestController
@RequestMapping(path = "/users/{userId}/locations")
public class PrivatePublicLocationController extends LocationController {
    public PrivatePublicLocationController(MainLocationService mainLocationService, EventService eventService) {
        super(mainLocationService, eventService);
        super.requester = "user:";
    }
}