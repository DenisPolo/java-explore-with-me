package ru.practicum.stat;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.stat.model.Stat;
import ru.practicum.statDto.StatHitDto;

@Mapper
public interface StatMapper {
    StatMapper INSTANCE = Mappers.getMapper(StatMapper.class);

    @Mapping(source = "timestamp", target = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    StatHitDto mapToStatHitDto(Stat stat);

    @Mapping(source = "timestamp", target = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Stat mapToNewStat(StatHitDto statHitDto);
}