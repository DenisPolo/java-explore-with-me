package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.dto.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;

    @NotNull(message = "'id' must not be null")
    private Long id;

    @NotNull(message = "'pinned' must not be null")
    private Boolean pinned;

    @NotBlank(message = "'title' field must not be empty")
    private String title;

    @Override
    public String toString() {
        return "CompilationDto"
                + "{"
                + "\"events\": " + events + ","
                + "\"id\": " + id + ","
                + "\"pinned\": " + pinned + ","
                + "\"title\": \"" + title + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompilationDto compilationDto = (CompilationDto) o;
        return events.equals(compilationDto.events)
                && id.equals(compilationDto.id)
                && pinned.equals(compilationDto.pinned)
                && title.equals(compilationDto.title);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (events == null) ? 0 : events.hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (pinned == null) ? 0 : pinned.hashCode();
        result += (title == null) ? 0 : title.hashCode();
        return result;
    }
}