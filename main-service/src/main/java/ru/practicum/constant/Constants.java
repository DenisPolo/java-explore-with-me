package ru.practicum.constant;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.from(ZoneOffset.UTC));

    public static final LocalDateTime CURRENT_TIME = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

    public static final String APP = "ewm-main-service";
}