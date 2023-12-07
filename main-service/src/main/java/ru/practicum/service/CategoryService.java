package ru.practicum.service;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.responseFormat.ResponseFormat;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories();

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long catId);

    CategoryDto updateCategory(long categoryId, NewCategoryDto newCategoryDto);

    ResponseFormat deleteCategory(long categoryId);
}