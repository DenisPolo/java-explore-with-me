package ru.practicum.statDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatDto {
    @NotBlank(message = "'app' field must not be empty")
    private String app;

    @NotBlank(message = "'uri' field must not be empty")
    private String uri;

    @NotNull(message = "'hits' field must not be empty")
    private Long hits;

    @Override
    public String toString() {
        return "StatDto"
                + "{\"app\": \"" + app
                + "\", \"uri\": \"" + uri
                + "\", \"hits\": \"" + hits
                + "\"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatDto other = (StatDto) o;
        return app.equals(other.app) && uri.equals(other.uri) && hits.equals(other.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }
}