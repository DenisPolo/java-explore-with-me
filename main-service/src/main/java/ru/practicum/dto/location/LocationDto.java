package ru.practicum.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationDto {
    private Float lat;
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