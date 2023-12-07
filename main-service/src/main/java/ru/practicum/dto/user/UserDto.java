package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;

    @Size(min = 6, message = "Email length must not be less than 6")
    @Size(max = 254, message = "Email length must not be more than 254")
    @Email(message = "'email' has wrong mail format")
    @NotBlank(message = "'email' field must not be empty")
    private String email;

    @Size(min = 2, message = "Name length must not be less than 2")
    @Size(max = 250, message = "Name length must not be more than 250")
    @NotBlank(message = "'name' field must not be empty")
    private String name;

    @Override
    public String toString() {
        return "UserDto"
                + "{\"id\": " + id + ","
                + "\"email\": \"" + email + "\","
                + "\"name\": \"" + name + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return email.equals(userDto.email)
                && id.equals(userDto.id)
                && name.equals(userDto.name);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (email == null) ? 0 : email.hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (name == null) ? 0 : name.hashCode();
        return result;
    }
}