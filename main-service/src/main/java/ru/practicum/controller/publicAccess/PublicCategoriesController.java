package ru.practicum.controller.publicAccess;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.log.Log;
import ru.practicum.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@AllArgsConstructor
public class PublicCategoriesController {
    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size,
            HttpServletRequest request) {
        Log.setRequestLog("public:", request);
        return ResponseEntity.ok().body(service.getCategories(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable long catId, HttpServletRequest request) {
        Log.setRequestLog("public:", request);
        return ResponseEntity.ok().body(service.getCategory(catId));
    }
}