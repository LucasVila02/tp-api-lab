package com.lucasvila.tp_api.exceptions;

import org.springframework.http.HttpStatus;

public class EmpleadoDuplicadoException extends RuntimeException {
    public EmpleadoDuplicadoException( String message) {super(message);
    }
}