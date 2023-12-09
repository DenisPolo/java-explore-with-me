package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        String defaultMessage;
        List<String> messages = new ArrayList<>(List.of(e.getMessage().split(";")));
        defaultMessage = messages.get(messages.size() - 1).replaceAll(".*\\[|\\].*", "");
        log.warn(defaultMessage);
        return new ApiError(List.of(Arrays.toString(e.getStackTrace())), HttpStatus.BAD_REQUEST.toString(),
                "Not valid argument presented", defaultMessage);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({BadRequestException.class,
            MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequestExceptionHandle(Exception e) {
        log.warn(e.getMessage());
        return new ApiError(List.of(Arrays.toString(e.getStackTrace())), HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request", e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({PSQLException.class, JDBCException.class,
            InternalServerErrorException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError internalServerErrorExceptionHandle(Exception e) {
        log.warn(e.getMessage());
        if (e.getMessage().contains("QueryState.")) {
            String[] splitMessage = e.getMessage().split("\\.");
            return new ApiError(List.of(Arrays.toString(e.getStackTrace())),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    "PSQL error",
                    "Unknown state: " + splitMessage[splitMessage.length - 1]);
        }
        if (e.getMessage().contains("SQL [")) {
            String[] splitMessage = e.getMessage().split("; ");
            return new ApiError(List.of(Arrays.toString(e.getStackTrace())),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    "JDBC: duplicate unique value violates unique constraint",
                    splitMessage[1] + ": " + splitMessage[0] + " " + splitMessage[2]);
        }
        return new ApiError(List.of(Arrays.toString(e.getStackTrace())), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "The server cannot process the request correctly", e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError alreadyExistsExceptionHandle(AlreadyExistsException e) {
        log.warn(e.getMessage());
        return new ApiError(List.of(Arrays.toString(e.getStackTrace())), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "The object with unique parameters already exists", e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundExceptionHandle(NotFoundException e) {
        log.warn(e.getMessage());
        return new ApiError(List.of(Arrays.toString(e.getStackTrace())), HttpStatus.NOT_FOUND.toString(),
                "The required object was not found", e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError forbiddenExceptionHandle(ForbiddenException e) {
        log.warn(e.getMessage());
        return new ApiError(List.of(Arrays.toString(e.getStackTrace())), HttpStatus.FORBIDDEN.toString(),
                "For the requested operation the conditions are not met", e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ConflictException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError conflictExceptionHandle(RuntimeException e) {
        log.warn(e.getMessage());
        return new ApiError(List.of(Arrays.toString(e.getStackTrace())), HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated", e.getMessage());
    }
}