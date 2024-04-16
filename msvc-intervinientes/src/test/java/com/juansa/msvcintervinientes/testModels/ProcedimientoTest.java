package com.juansa.msvcintervinientes.testModels;

import com.juansa.msvcintervinientes.models.Procedimiento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcedimientoTest {

    @Test
    public void testGetAndSetId() {
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);
        assertEquals(1L, procedimiento.getId());
    }

    @Test
    public void testGetAndSetNumeroProcedimiento() {
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setNumeroProcedimiento("1234");
        assertEquals("1234", procedimiento.getNumeroProcedimiento());
    }

    @Test
    public void testGetAndSetAnno() {
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setAnno(2022);
        assertEquals(2022, procedimiento.getAnno());
    }
}
