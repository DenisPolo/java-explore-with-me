package ru.practicum.statDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class StatHitDto {
    private Long id;

    @NotBlank(message = "'app' field must not be empty")
    private String app;

    @NotBlank(message = "'uri' field must not be empty")
    private String uri;

    @NotBlank(message = "'ip' field must not be empty")
    private String ip;

    @NotBlank(message = "'timestamp' field must not be empty")
    private String timestamp;

    public StatHitDto(String app, String uri, String ip, String timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "StatHitDto"
                + "{\"id\": " + id
                + ", \"app\": \"" + app
                + "\", \"uri\": \"" + uri
                + "\", \"ip\": \"" + ip
                + "\", \"timestamp\": \"" + timestamp
                + "\"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatHitDto other = (StatHitDto) o;
        return app.equals(other.app) && uri.equals(other.uri) && ip.equals(other.ip)
                && timestamp.equals(other.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, ip, timestamp);
    }
}