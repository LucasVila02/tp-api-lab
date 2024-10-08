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
        // Mapa para almacenar los errores de validación de cada campo.
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });
        // Cuerpo de la respuesta que incluye el estado y los errores de los campos.
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("errors", fieldErrors);

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            EmpleadoDuplicadoException.class,
            FechaInvalidaException.class,
            EdadInvalidaException.class,
            NoEncontradoException.class,
            BadRequestException.class,
            NroDocumentoInvalidoException.class,
            HorasTurnosInvalidoException.class
    })
    public ResponseEntity<Object> handleBussinessException(RuntimeException ex, WebRequest request) {
        HttpStatus status;

        // Asigna el código de estado basado en el tipo de excepción.
        if (ex instanceof EmpleadoDuplicadoException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof NoEncontradoException || ex instanceof NroDocumentoInvalidoException) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        return buildResponseEntity(status, ex.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("status", status.value());
        responseBody.put("message", message);

        return new ResponseEntity<>(responseBody, status);
    }
}

