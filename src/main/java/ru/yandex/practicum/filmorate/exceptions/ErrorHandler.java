package ru.yandex.practicum.filmorate.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String validationException(ValidationException exception) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FilmNotFoundException.class)
    public String filmNotFoundException(FilmNotFoundException exception) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String userNotFoundException(UserNotFoundException exception) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MpaNotFoundException.class)
    public String mpaNotFoundException(MpaNotFoundException exception) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GenreNotFoundException.class)
    public String genreNotFoundException(GenreNotFoundException exception) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }
}
