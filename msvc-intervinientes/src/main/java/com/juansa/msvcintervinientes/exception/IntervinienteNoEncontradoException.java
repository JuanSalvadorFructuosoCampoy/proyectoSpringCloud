package com.juansa.msvcintervinientes.exception;

public class IntervinienteNoEncontradoException extends RuntimeException {
    public IntervinienteNoEncontradoException() {
        super("No se encontró el interviniente.");
    }
}
