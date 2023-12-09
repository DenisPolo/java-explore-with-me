package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserShortDto {

    @NotNull(message = "'id' must not be null")
    private Long id;

    @NotBlank(message = "'email' field must not be empty")
    private String name;

    @Override
    public String toString() {
        return "UserShorDto"
                + "{\"id\": " + id + ","
                + "\"name\": \"" + name + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserShortDto userShortDto = (UserShortDto) o;
        return id.equals(userShortDto.id)
                && name.equals(userShortDto.name);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (name == null) ? 0 : name.hashCode();
        return result;
    }
}