package ru.practicum.controller.publicAccess;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.controller.abstractControllers.LocationController;
import ru.practicum.service.EventService;
import ru.practicum.service.MainLocationService;

@RestController
@RequestMapping(path = "/locations")
public class PublicLocationController extends LocationController {
    public PublicLocationController(MainLocationService mainLocationService, EventService eventService) {
        super(mainLocationService, eventService);
        super.requester = "public:";
    }
}