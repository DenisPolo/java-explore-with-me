package ru.practicum.stat.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseFormat methodArgumentNotValidExceptionHandle(Exception e) {
        String defaultMessage;

        List<String> messages = new ArrayList<>(List.of(e.getMessage().split(";")));
        defaultMessage = messages.get(messages.size() - 1).replaceAll(".*\\[|\\].*", "");
        log.warn(HttpStatus.BAD_REQUEST + ": " + defaultMessage);
        return new ErrorResponseFormat(defaultMessage, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseFormat badRequestExceptionHandle(BadRequestException e) {
        log.warn(HttpStatus.BAD_REQUEST + ": " + e.getMessage());
        return new ErrorResponseFormat(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({PSQLException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseFormat internalServerErrorExceptionHandle(Exception e) {
        log.warn(HttpStatus.INTERNAL_SERVER_ERROR + ": " + e.getMessage());
        if (e.getMessage().contains("QueryState.")) {
            String[] splitMessage = e.getMessage().split("\\.");
            return new ErrorResponseFormat("Unknown state: " + splitMessage[splitMessage.length - 1],
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ErrorResponseFormat(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}