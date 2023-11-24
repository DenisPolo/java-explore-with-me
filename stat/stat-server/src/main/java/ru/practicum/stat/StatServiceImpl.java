package ru.practicum.stat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat.exception.BadRequestException;
import ru.practicum.stat.model.Stat;
import ru.practicum.stat.queryParams.QueryParams;
import ru.practicum.statDto.StatDto;
import ru.practicum.statDto.StatHitDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public StatHitDto postHit(StatHitDto statHitDto) {
        Stat statHit = StatMapper.INSTANCE.mapToNewStat(statHitDto);
        return StatMapper.INSTANCE.mapToStatHitDto(statRepository.save(statHit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatDto> getStat(QueryParams params) {

        if (params.getStart().isAfter(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC))) {
            throw new BadRequestException("Start time is in the future");
        }

        if (params.getStart().isAfter(params.getEnd())) {
            throw new BadRequestException("Start time is after end time");
        }

        if (params.getUris() != null) {
            return params.getUnique() ?
                    statRepository.findStatsUniqueUris(params.getUris(), params.getStart(), params.getEnd()) :
                    statRepository.findStatsUris(params.getUris(), params.getStart(), params.getEnd());
        } else {
            return params.getUnique() ?
                    statRepository.findStatsUnique(params.getStart(), params.getEnd()) :
                    statRepository.findStats(params.getStart(), params.getEnd());
        }
    }
}