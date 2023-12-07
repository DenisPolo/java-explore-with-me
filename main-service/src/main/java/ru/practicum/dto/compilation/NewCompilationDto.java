package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;

    @NotBlank(message = "'title' field must not be empty")
    @Size(min = 1, message = "Title length must not be less than 1")
    @Size(max = 50, message = "Title length must not be more than 50")
    private String title;

    @Override
    public String toString() {
        return "NewCompilationDto"
                + "{"
                + "\"events\": " + events + ","
                + "\"pinned\": " + pinned + ","
                + "\"title\": \"" + title + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewCompilationDto newCompilationDto = (NewCompilationDto) o;
        return events.equals(newCompilationDto.events)
                && pinned.equals(newCompilationDto.pinned)
                && title.equals(newCompilationDto.title);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (events == null) ? 0 : events.hashCode();
        result += (pinned == null) ? 0 : pinned.hashCode();
        result += (title == null) ? 0 : title.hashCode();
        return result;
    }
}