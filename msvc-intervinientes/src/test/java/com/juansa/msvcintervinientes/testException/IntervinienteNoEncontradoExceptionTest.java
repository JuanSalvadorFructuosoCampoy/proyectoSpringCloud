package com.juansa.msvcintervinientes.testException;

import com.juansa.msvcintervinientes.exception.IntervinienteNoEncontradoException;
import com.juansa.msvcintervinientes.services.IntervinienteService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class IntervinienteNoEncontradoExceptionTest {

    @MockBean
    private IntervinienteService intervienienteService;

    @Test
    void testPorIdNotFound() {
        when(intervienienteService.porId(10L)).thenThrow(new IntervinienteNoEncontradoException());

        assertThrows(IntervinienteNoEncontradoException.class, () -> intervienienteService.porId(10L));
    }
}
