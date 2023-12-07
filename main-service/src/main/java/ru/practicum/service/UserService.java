package ru.practicum.service;

import ru.practicum.dto.user.UserDto;
import ru.practicum.responseFormat.ResponseFormat;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    ResponseFormat deleteUser(long userId);
}