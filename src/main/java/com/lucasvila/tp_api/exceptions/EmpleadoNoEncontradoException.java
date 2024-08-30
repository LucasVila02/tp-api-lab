package com.lucasvila.tp_api.exceptions;

public class EmpleadoNoEncontradoException extends RuntimeException {
    public EmpleadoNoEncontradoException(Long id) {
        super("No se encontró el empleado con Id: " + id);
    }
}
