package com.lucasvila.tp_api.exceptions;

public class NoEncontradoException extends RuntimeException {
    public NoEncontradoException(Long id, String str) {
        super("No se encontró el " + str  + " con Id: " + id);
    }
}
