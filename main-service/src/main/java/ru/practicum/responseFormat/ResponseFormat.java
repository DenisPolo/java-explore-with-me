package ru.practicum.responseFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static ru.practicum.constant.Constants.CURRENT_TIME;

@Getter
public class ResponseFormat {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime time = CURRENT_TIME;
    private final HttpStatus status;
    private final String message;

    public ResponseFormat(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}