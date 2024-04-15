package com.juansa.msvcprocedimientos.testexception;

import com.juansa.msvcprocedimientos.exception.NumeroDuplicadoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumeroDuplicadoExceptionTest {
    @Test
    void testExceptionMessage() {
        NumeroDuplicadoException exception = new NumeroDuplicadoException();
        assertEquals("Ya existe un procedimiento con ese número.", exception.getMessage());
    }
}
