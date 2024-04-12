package com.juansa.msvcprocedimientos.exception;

public class NumeroDuplicadoException extends RuntimeException{
    public NumeroDuplicadoException() {
        super("Ya existe un procedimiento con ese número.");
    }
}
