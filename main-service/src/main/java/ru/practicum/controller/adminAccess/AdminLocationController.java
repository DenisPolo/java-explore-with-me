package ru.practicum.controller.adminAccess;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.controller.abstractControllers.LocationController;
import ru.practicum.dto.location.MainLocationDto;
import ru.practicum.log.Log;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.EventService;
import ru.practicum.service.MainLocationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/admin/locations")
public class AdminLocationController extends LocationController {
    public AdminLocationController(MainLocationService mainLocationService, EventService eventService) {
        super(mainLocationService, eventService);
        super.requester = "admin:";
    }

    @PostMapping
    public ResponseEntity<MainLocationDto> createLocation(@Valid @RequestBody MainLocationDto newLocationDto,
                                                          HttpServletRequest request) {
        Log.setRequestLog(requester, request);
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(mainLocationService.createLocation(newLocationDto));
    }

    @PatchMapping
    public ResponseEntity<MainLocationDto> updateEvent(
            @Valid @RequestBody MainLocationDto updateLocationDto,
            HttpServletRequest request) {
        Log.setRequestLog(requester, request);
        return ResponseEntity.ok().body(mainLocationService.updateLocation(updateLocationDto));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<ResponseFormat> deleteLocation(@PathVariable Long locationId, HttpServletRequest request) {
        Log.setRequestLog(requester, request);
        ResponseFormat response = mainLocationService.deleteLocation(locationId);
        return ResponseEntity.noContent().header("X-Deleted-User-Info", response.getMessage()).build();
    }
}