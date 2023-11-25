package ru.practicum.stat;

import ru.practicum.stat.queryParams.QueryParams;
import ru.practicum.statDto.StatDto;
import ru.practicum.statDto.StatHitDto;

import java.util.List;

public interface StatService {
    StatHitDto postHit(StatHitDto statHitDto);

    List<StatDto> getStat(QueryParams params);
}