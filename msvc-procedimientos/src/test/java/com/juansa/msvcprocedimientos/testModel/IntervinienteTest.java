package com.juansa.msvcprocedimientos.testModel;

import com.juansa.msvcprocedimientos.models.Interviniente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntervinienteTest {

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String nombre = "Antonio Campillo";
        String tipoIntervencion = "Tipo 1";
        Long procedimientoId = 1L;

        Interviniente interviniente = new Interviniente(id, nombre, tipoIntervencion, procedimientoId);

        assertEquals(id, interviniente.getId());
        assertEquals(tipoIntervencion, interviniente.getTipoIntervencion());
        assertEquals(procedimientoId, interviniente.getProcedimientoId());
    }
}
