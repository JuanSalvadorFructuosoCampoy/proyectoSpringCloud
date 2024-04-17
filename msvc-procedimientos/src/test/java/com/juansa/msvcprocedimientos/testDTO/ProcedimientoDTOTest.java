package com.juansa.msvcprocedimientos.testDTO;

import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcedimientoDTOTest {

    @Mock
    private ProcedimientoDTO procedimientoDTO;

    @BeforeEach
    void setUp() {
        procedimientoDTO = new ProcedimientoDTO();
    }

    @Test
    void testId() {
        Long idValue = 1L;
        procedimientoDTO.setId(idValue);
        assertEquals(idValue, procedimientoDTO.getId());
    }

    @Test
    void testNumeroProcedimiento() {
        String numeroValue = "PR0001";
        procedimientoDTO.setNumeroProcedimiento(numeroValue);
        assertEquals(numeroValue, procedimientoDTO.getNumeroProcedimiento());
    }

    @Test
    void testAnno() {
        int anno = 2024;
        procedimientoDTO.setAnno(anno);
        assertEquals(anno, procedimientoDTO.getAnno());
    }
}
