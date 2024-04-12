package com.juansa.msvcintervinientes.testcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juansa.msvcintervinientes.controller.IntervinienteController;
import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.entities.Interviniente;
import com.juansa.msvcintervinientes.services.IntervinienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
 class IntervinienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IntervinienteService intervinienteService;

    private Interviniente interviniente;

    @BeforeEach
     void setup() {
        interviniente = new Interviniente();
        interviniente.setId(1L);
        interviniente.setNombre("Nombre");
        interviniente.setTipoIntervencion("Tipo");
    }

    @Test
     void testGetById() throws Exception {
        when(intervinienteService.porId(anyLong())).thenReturn(Optional.of(interviniente));

        mockMvc.perform(get("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
     void testListar() throws Exception {
        when(intervinienteService.listar()).thenReturn(Arrays.asList(interviniente));

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
     void testCrear() throws Exception {
        IntervinienteDTO intervinienteDTO = new IntervinienteDTO();
        intervinienteDTO.setNombre("Nombre");
        intervinienteDTO.setTipoIntervencion("Tipo");

        when(intervinienteService.guardarNuevo(any(IntervinienteDTO.class))).thenReturn(interviniente);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(intervinienteDTO)))
                .andExpect(status().isCreated());
    }

    @Test
     void testEditar() throws Exception {
        IntervinienteDTO intervinienteDTO = new IntervinienteDTO();
        intervinienteDTO.setNombre("Nombre");
        intervinienteDTO.setTipoIntervencion("Tipo");

        when(intervinienteService.porId(anyLong())).thenReturn(Optional.of(interviniente));
        when(intervinienteService.guardarEditar(any(Interviniente.class))).thenReturn(interviniente);

        mockMvc.perform(put("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(intervinienteDTO)))
                .andExpect(status().isCreated());
    }

    @Test
     void testEliminar() throws Exception {
        when(intervinienteService.porId(anyLong())).thenReturn(Optional.of(interviniente));

        mockMvc.perform(delete("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
     void testUrlPostIncorrecta() throws Exception {
        mockMvc.perform(post("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
     void testUrlPutIncorrecta() throws Exception {
        mockMvc.perform(put("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
     void testUrlDeleteIncorrecta() throws Exception {
        mockMvc.perform(delete("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}