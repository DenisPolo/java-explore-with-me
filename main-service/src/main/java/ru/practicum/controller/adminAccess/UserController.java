package ru.practicum.controller.adminAccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        log.info("admin: " + "(" + request.getMethod() + ")" + request.getRequestURL());
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(service.createUser(userDto));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size,
            HttpServletRequest request) {
        String requestParams = request.getQueryString();
        log.info("admin: " + "(" + request.getMethod() + ")" + request.getRequestURL()
                + (requestParams == null ? "" : "?" + requestParams));
        return ResponseEntity.ok().body(service.getUsers(ids, from, size));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseFormat> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        log.info("admin: " + "(" + request.getMethod() + ")" + request.getRequestURL());
        ResponseFormat response = service.deleteUser(userId);
        return ResponseEntity.noContent().header("X-Deleted-User-Info", response.getMessage()).build();
    }
}