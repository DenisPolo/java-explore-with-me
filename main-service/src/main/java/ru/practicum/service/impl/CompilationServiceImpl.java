package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.responseFormat.ResponseFormat;
import ru.practicum.service.CompilationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events;
        Compilation compilation;

        if (newCompilationDto.getEvents() != null) {
            events = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        } else {
            events = new HashSet<>();
        }

        compilation = new Compilation(events, newCompilationDto.getPinned() != null && newCompilationDto.getPinned(),
                newCompilationDto.getTitle());

        return CompilationMapper.INSTANCE.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size > 0 ? size : 10);

        if (pinned == null) {
            return CompilationMapper.INSTANCE.mapToCompilationDto(compilationRepository.findAll(page).getContent());
        } else {
            return CompilationMapper.INSTANCE.mapToCompilationDto(compilationRepository
                    .findCompilationsByPinned(pinned, page));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(long compId) {
        return CompilationMapper.INSTANCE.mapToCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with ID: " + compId + " doesn't exist")));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Set<Event> events;
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with ID: " + compId + " doesn't exist"));

        if (updateCompilationRequest.getEvents() != null) {
            events = new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents()));
        } else {
            events = new HashSet<>();
        }

        compilation.setEvents(events);

        if (updateCompilationRequest.getPinned() != null) compilation.setPinned(updateCompilationRequest.getPinned());
        if (updateCompilationRequest.getTitle() != null) compilation.setTitle(updateCompilationRequest.getTitle());

        return CompilationMapper.INSTANCE.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public ResponseFormat deleteCompilation(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with ID: " + compId + " doesn't exist"));

        compilation.setEvents(new HashSet<>());

        compilationRepository.save(compilation);

        compilationRepository.deleteById(compId);

        if (compilationRepository.existsById(compId)) {
            String message = "Unknown error. Compilation with ID: " + compId + " was not deleted";
            log.warn(message);
            throw new BadRequestException(message);
        } else {
            String message = "Compilation with ID: " + compId + " successfully deleted";
            log.info(message);
            return new ResponseFormat(message, HttpStatus.OK);
        }
    }
}