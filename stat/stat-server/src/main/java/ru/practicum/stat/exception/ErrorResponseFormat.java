package ru.practicum.stat.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
public class ErrorResponseFormat {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime time = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    private final HttpStatus status;

    private final String error;

    public ErrorResponseFormat(String message, HttpStatus status) {
        this.error = message;
        this.status = status;
    }
}