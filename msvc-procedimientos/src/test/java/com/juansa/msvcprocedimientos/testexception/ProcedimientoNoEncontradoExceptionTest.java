package com.juansa.msvcprocedimientos.testexception;

import com.juansa.msvcprocedimientos.exception.ProcedimientoNoEncontradoException;
import com.juansa.msvcprocedimientos.services.ProcedimientoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProcedimientoNoEncontradoExceptionTest {

    @Mock
    private ProcedimientoService intervienienteService;

    @Test
    void testPorIdNotFound() {
        when(intervienienteService.porId(10L)).thenThrow(new ProcedimientoNoEncontradoException());

        assertThrows(ProcedimientoNoEncontradoException.class, () -> intervienienteService.porId(10L));
    }
}
