package ru.practicum.controller.adminAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.log.Log;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(service.createUser(userDto));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.ok().body(service.getUsers(ids, from, size));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseFormat> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        ResponseFormat response = service.deleteUser(userId);
        return ResponseEntity.noContent().header("X-Deleted-User-Info", response.getMessage()).build();
    }
}