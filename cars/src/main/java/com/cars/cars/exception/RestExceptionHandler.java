package com.cars.cars.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex) {
        return ex.getMessage();
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException ex) {
        return ex.getMessage();
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex) {
        return "Ocurrió un error";
    }
}
