package ru.practicum.controller.adminAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.log.Log;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService service;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(
            @Valid @RequestBody NewCompilationDto newCompilationDto,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.created(URI.create(request.getRequestURI()))
                .body(service.createCompilation(newCompilationDto));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable long compId,
            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest,
            HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        return ResponseEntity.ok().body(service.updateCompilation(compId, updateCompilationRequest));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<ResponseFormat> deleteCompilation(@PathVariable long compId, HttpServletRequest request) {
        Log.setRequestLog("admin:", request);
        ResponseFormat response = service.deleteCompilation(compId);
        return ResponseEntity.noContent().header("X-Deleted-Compilation-Info", response.getMessage()).build();
    }
}