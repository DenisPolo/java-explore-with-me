package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.User;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDto(Iterable<User> users);

    User mapToNewUser(UserDto userDto);
}