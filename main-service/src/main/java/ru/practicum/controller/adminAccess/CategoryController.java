package ru.practicum.controller.adminAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.log.Log;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody NewCategoryDto newCategoryDto,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(service.createCategory(newCategoryDto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.ok().body(service.getCategories());
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable long catId,
            @Valid @RequestBody NewCategoryDto newCategoryDto,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.ok().body(service.updateCategory(catId, newCategoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<ResponseFormat> deleteCategory(@PathVariable long catId, HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        ResponseFormat response = service.deleteCategory(catId);
        return ResponseEntity.noContent().header("X-Deleted-Category-Info", response.getMessage()).build();
    }
}