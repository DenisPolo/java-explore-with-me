package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

import static ru.practicum.constant.Constants.DATE_TIME_FORMATTER;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "main_locations", schema = "public")
public class MainLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "lat", nullable = false)
    private Float lat;

    @Column(name = "lon", nullable = false)
    private Float lon;

    @Column(name = "rad", nullable = false)
    private Float rad;

    @Column(name = "created_on")
    private Instant createdOn = Instant.now();

    @Override
    public String toString() {
        return "Location"
                + "{"
                + "\"id\": " + id + ","
                + "\"name\": \"" + name + "\","
                + "\"name\": \"" + description + "\","
                + "\"id\": " + id + ","
                + "\"lat\": " + lat + ","
                + "\"lon\": " + lon + ","
                + "\"lon\": " + rad + ","
                + "\"createdOn\": \"" + DATE_TIME_FORMATTER.format(createdOn) + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainLocation location = (MainLocation) o;
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