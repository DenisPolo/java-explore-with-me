package ru.practicum.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
public class MainLocationDto {
    private Long id;

    @NotBlank(message = "'name' field must not be empty")
    @Size(min = 1, message = "Main location name length must not be less than 1")
    @Size(max = 50, message = "Main location name length must not be greater than 50")
    private String name;

    @Size(min = 20, message = "Main location description length must not be less than 20")
    @Size(max = 7000, message = "Main location description length must not be greater than 7000")
    private String description;

    @Min(value = -90, message = "Main location latitude must not be less than -90°")
    @Max(value = 90, message = "Main location latitude must not be greater than 90°")
    @NotNull(message = "Main location latitude must not be null")
    private Float lat;

    @Min(value = -180, message = "Main location longitude must not be less than -180°")
    @Max(value = 180, message = "Main location longitude must not be greater than 180°")
    @NotNull(message = "Main location longitude must not be null")
    private Float lon;

    @Min(value = 0, message = "Main location radius must not be less than 0 meters")
    @Max(value = 100000, message = "Main location radius must not be greater than 100 000 meters")
    private Float rad;

    @Override
    public String toString() {
        return "Location"
                + "{"
                + "\"id\": " + id + ","
                + "\"name\": \"" + name + "\","
                + "\"name\": \"" + description + "\","
                + "\"lat\": " + lat + ","
                + "\"lon\": " + lon
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainLocationDto location = (MainLocationDto) o;
        return id.equals(location.id)
                && name.equals(location.name)
                && description.equals(location.description)
                && lat.equals(location.lat)
                && lon.equals(location.lon)
                && rad.equals(location.rad);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (name == null) ? 0 : name.hashCode();
        result += (description == null) ? 0 : description.hashCode();
        result += (lat == null) ? 0 : lat.hashCode();
        result += (lon == null) ? 0 : lon.hashCode();
        result += (rad == null) ? 0 : rad.hashCode();
        return result;
    }
}