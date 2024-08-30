package com.lucasvila.tp_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.util.*;

@ControllerAdvice
public class EmpleadoControllerException  {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("errors", fieldErrors);

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmpleadoDuplicadoException.class)
    public ResponseEntity<Object> handleBussinessException(EmpleadoDuplicadoException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(FechaInvalidaException.class)
    public ResponseEntity<Object> handleBussinessException(FechaInvalidaException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EdadInvalidaException.class)
    public ResponseEntity<Object> handleBussinessException(EdadInvalidaException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ResponseEntity<Object> handleBussinessException(EmpleadoNoEncontradoException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", new Date());
        responseBody.put("status", status.value());
        responseBody.put("message", message);

        return new ResponseEntity<>(responseBody, status);
    }

}

