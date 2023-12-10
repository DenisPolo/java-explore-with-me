package ru.practicum.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@AllArgsConstructor
public class LocationDto {

    @Min(value = -90, message = "Location latitude must not be less than -90°")
    @Max(value = 90, message = "Location latitude must not be greater than 90°")
    private Float lat;

    @Min(value = -180, message = "Location latitude must not be less than -180°")
    @Max(value = 180, message = "Location latitude must not be greater than 180°")
    private Float lon;

    @Override
    public String toString() {
        return "Location"
                + "{"
                + "\"lat\": " + lat + ","
                + "\"lon\": " + lon
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationDto location = (LocationDto) o;
        return lat.equals(location.lat)
                && lon.equals(location.lon);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (lat == null) ? 0 : lat.hashCode();
        result += (lon == null) ? 0 : lon.hashCode();
        return result;
    }
}