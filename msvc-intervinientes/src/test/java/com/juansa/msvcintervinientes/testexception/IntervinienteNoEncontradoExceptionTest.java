package com.juansa.msvcintervinientes.testexception;

import com.juansa.msvcintervinientes.exception.IntervinienteNoEncontradoException;
import com.juansa.msvcintervinientes.services.IntervinienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class IntervinienteNoEncontradoExceptionTest {

    @Mock
    private IntervinienteService intervienienteService;

    @Test
    void testPorIdNotFound() {
        when(intervienienteService.porId(10L)).thenThrow(new IntervinienteNoEncontradoException());

        assertThrows(IntervinienteNoEncontradoException.class, () -> intervienienteService.porId(10L));
    }
}
