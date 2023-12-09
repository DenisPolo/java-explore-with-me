package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto mapToCategoryDto(Category category);

    List<CategoryDto> mapToCategoryDto(Iterable<Category> categories);

    Category mapToNewCategory(NewCategoryDto newCategoryDto);
}