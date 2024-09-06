package com.lucasvila.tp_api.exceptions;

public class NoEncontradoException extends RuntimeException {
    public NoEncontradoException(Long id, String str) {
        super("No existe el " + str  + " con Id: " + id);
    }
}
