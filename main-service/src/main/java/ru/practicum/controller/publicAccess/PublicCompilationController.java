package ru.practicum.controller.publicAccess;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.log.Log;
import ru.practicum.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@AllArgsConstructor
public class PublicCompilationController {
    private final CompilationService service;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size,
            HttpServletRequest request) {
        Log.setRequestLog("public:", request);
        return ResponseEntity.ok().body(service.getCompilations(pinned, from, size));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable long compId, HttpServletRequest request) {
        Log.setRequestLog("public:", request);
        return ResponseEntity.ok().body(service.getCompilation(compId));
    }
}