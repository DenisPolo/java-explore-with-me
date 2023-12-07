package ru.practicum.controller.adminAccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody NewCategoryDto newCategoryDto,
            HttpServletRequest request) {
        log.info("admin: " + "(" + request.getMethod() + ")" + request.getRequestURL());
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(service.createCategory(newCategoryDto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(HttpServletRequest request) {
        String requestParams = request.getQueryString();
        log.info("admin: " + "(" + request.getMethod() + ")" + request.getRequestURL()
                + (requestParams == null ? "" : "?" + requestParams));
        return ResponseEntity.ok().body(service.getCategories());
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable long catId,
            @Valid @RequestBody NewCategoryDto newCategoryDto,
            HttpServletRequest request) {
        log.info("admin: " + "(" + request.getMethod() + ")" + request.getRequestURL());
        return ResponseEntity.ok().body(service.updateCategory(catId, newCategoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<ResponseFormat> deleteCategory(@PathVariable long catId, HttpServletRequest request) {
        log.info("admin: " + "(" + request.getMethod() + ")" + request.getRequestURL());
        ResponseFormat response = service.deleteCategory(catId);
        return ResponseEntity.noContent().header("X-Deleted-Category-Info", response.getMessage()).build();
    }
}