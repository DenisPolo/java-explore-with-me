package ru.practicum.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "'name' field must not be empty")
    @Size(min = 1, message = "Category name length must not be less than 1")
    @Size(max = 50, message = "Category name length must not be more than 50")
    private String name;

    @Override
    public String toString() {
        return "CategoryDto"
                + "{\"id\": " + id + ","
                + "\"name\": \"" + name + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDto categoryDto = (CategoryDto) o;
        return id.equals(categoryDto.id)
                && name.equals(categoryDto.name);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (id == null) ? 0 : id.hashCode();
        result += (name == null) ? 0 : name.hashCode();
        return result;
    }
}