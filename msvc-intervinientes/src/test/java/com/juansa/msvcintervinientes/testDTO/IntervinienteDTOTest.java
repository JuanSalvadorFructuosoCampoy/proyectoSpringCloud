package com.juansa.msvcintervinientes.testDTO;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntervinienteDTOTest {
    private IntervinienteDTO intervienienteDTO;

    @BeforeEach
    void setUp() {
        intervienienteDTO = new IntervinienteDTO();
    }

    @Test
    void testId() {
        Long idValue = 1L;
        intervienienteDTO.setId(idValue);
        assertEquals(idValue, intervienienteDTO.getId());
    }

    @Test
    void testNombre() {
        String nombreValue = "Test Nombre";
        intervienienteDTO.setNombre(nombreValue);
        assertEquals(nombreValue, intervienienteDTO.getNombre());
    }

    @Test
    void testTipoIntervencion() {
        String tipoIntervencionValue = "Test Tipo Intervencion";
        intervienienteDTO.setTipoIntervencion(tipoIntervencionValue);
        assertEquals(tipoIntervencionValue, intervienienteDTO.getTipoIntervencion());
    }
}
