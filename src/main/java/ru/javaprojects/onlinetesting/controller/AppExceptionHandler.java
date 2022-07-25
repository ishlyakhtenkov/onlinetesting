package ru.javaprojects.onlinetesting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class AppExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class, MissingRequestValueException.class})
    public String handleBadRequestException(Exception e) {
        log.warn(e.getMessage());
        return "error";
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error(e.getMessage());
        return "error";
    }
}