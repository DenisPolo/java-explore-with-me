package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.UserService;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.mapToNewUser(userDto);

        return UserMapper.INSTANCE.mapToUserDto(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size > 0 ? size : 10);

        if (ids == null) {
            return UserMapper.INSTANCE.mapToUserDto(repository.findAll(page));
        }

        return UserMapper.INSTANCE.mapToUserDto(repository.findUserByIdInOrderByIdAsc(ids, page));
    }

    @Override
    @Transactional
    public ResponseFormat deleteUser(long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
        } else {
            throw new BadRequestException("User with ID: " + userId + " doesn't exist");
        }

        if (repository.existsById(userId)) {
            String message = "Unknown error. User with ID: " + userId + " was not deleted";
            log.warn(message);
            throw new BadRequestException(message);
        } else {
            String message = "User with ID: " + userId + " successfully deleted";
            log.info(message);
            return new ResponseFormat(message, HttpStatus.OK);
        }
    }
}