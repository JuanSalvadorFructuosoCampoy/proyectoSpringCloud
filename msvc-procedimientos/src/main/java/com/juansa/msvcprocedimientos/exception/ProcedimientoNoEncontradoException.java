package com.juansa.msvcprocedimientos.exception;

public class ProcedimientoNoEncontradoException extends RuntimeException{
    public ProcedimientoNoEncontradoException() {
        super("No se encontró el procedimiento.");
    }
}
