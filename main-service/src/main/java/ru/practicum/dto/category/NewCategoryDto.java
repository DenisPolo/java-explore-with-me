package ru.practicum.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = "'name' field must not be empty")
    @Size(min = 1, message = "category name length must not be less than 1")
    @Size(max = 50, message = "category name length must not be more than 50")
    private String name;

    @Override
    public String toString() {
        return "NewCategoryDto"
                + "{\"name\": \"" + name + "\""
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewCategoryDto newCategoryDto = (NewCategoryDto) o;
        return name.equals(newCategoryDto.name);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result += (name == null) ? 0 : name.hashCode();
        return result;
    }
}