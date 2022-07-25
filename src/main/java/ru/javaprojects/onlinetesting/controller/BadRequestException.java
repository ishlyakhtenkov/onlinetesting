package ru.javaprojects.onlinetesting.controller;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
