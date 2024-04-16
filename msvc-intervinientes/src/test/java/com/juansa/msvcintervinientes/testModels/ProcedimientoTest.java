package com.juansa.msvcintervinientes.testModels;

import com.juansa.msvcintervinientes.models.Procedimiento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcedimientoTest {

    @Test
    void testGetAndSetId() {
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);
        assertEquals(1L, procedimiento.getId());
    }

    @Test
    void testGetAndSetNumeroProcedimiento() {
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setNumeroProcedimiento("1234");
        assertEquals("1234", procedimiento.getNumeroProcedimiento());
    }

    @Test
    void testGetAndSetAnno() {
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setAnno(2022);
        assertEquals(2022, procedimiento.getAnno());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String numeroProcedimiento = "1234";
        int anno = 2022;

        Procedimiento procedimiento = new Procedimiento(id, numeroProcedimiento, anno);

        assertEquals(id, procedimiento.getId());
        assertEquals(numeroProcedimiento, procedimiento.getNumeroProcedimiento());
        assertEquals(anno, procedimiento.getAnno());
    }
}
