package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.CategoryService;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.INSTANCE.mapToNewCategory(newCategoryDto);

        return CategoryMapper.INSTANCE.mapToCategoryDto(repository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories() {
        return CategoryMapper.INSTANCE.mapToCategoryDto(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size > 0 ? size : 10);
        return CategoryMapper.INSTANCE.mapToCategoryDto(repository.findAll(page));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(long catId) {
        return CategoryMapper.INSTANCE.mapToCategoryDto(repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with ID: " + catId + " doesn't exist")));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(long categoryId, NewCategoryDto newCategoryDto) {
        Category updatedCategory = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID: " + categoryId + " doesn't exist"));

        if (updatedCategory.getName().equals(newCategoryDto.getName())) {
            return CategoryMapper.INSTANCE.mapToCategoryDto(updatedCategory);
        }

        updatedCategory.setName(newCategoryDto.getName());

        return CategoryMapper.INSTANCE.mapToCategoryDto(repository.save(repository.save(updatedCategory)));
    }

    @Override
    @Transactional
    public ResponseFormat deleteCategory(long categoryId) {
        if (repository.existsById(categoryId)) {
            repository.deleteById(categoryId);
        } else {
            throw new BadRequestException("Category with ID: " + categoryId + " doesn't exist");
        }

        if (repository.existsById(categoryId)) {
            String message = "Unknown error. Category with ID: " + categoryId + " was not deleted";

            log.warn(message);

            throw new BadRequestException(message);
        } else {
            String message = "Category with ID: " + categoryId + " successfully deleted";

            log.info(message);

            return new ResponseFormat(message, HttpStatus.OK);
        }
    }
}