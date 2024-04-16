package com.juansa.msvcintervinientes.testController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.models.entity.Interviniente;
import com.juansa.msvcintervinientes.services.IntervinienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

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
   void testCrearError() throws Exception {
      IntervinienteDTO intervinienteDTO = new IntervinienteDTO();
      intervinienteDTO.setNombre("");
      intervinienteDTO.setTipoIntervencion("Tipo");

      when(intervinienteService.guardarNuevo(any(IntervinienteDTO.class))).thenReturn(interviniente);

      mockMvc.perform(post("/")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(new ObjectMapper().writeValueAsString(intervinienteDTO)))
              .andExpect(status().isBadRequest());
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
   void testEditarNotFound() throws Exception {
      IntervinienteDTO intervinienteDTO = new IntervinienteDTO();
      intervinienteDTO.setNombre("Nombre");
      intervinienteDTO.setTipoIntervencion("Tipo");

      when(intervinienteService.porId(10L)).thenReturn(Optional.empty());
      when(intervinienteService.guardarEditar(any(Interviniente.class))).thenReturn(interviniente);

      mockMvc.perform(put("/10")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(new ObjectMapper().writeValueAsString(intervinienteDTO)))
              .andExpect(status().isNotFound());
   }

   @Test
   void testEditarError() throws Exception {
      IntervinienteDTO intervinienteDTO = new IntervinienteDTO();
      intervinienteDTO.setNombre("");
      intervinienteDTO.setTipoIntervencion("Tipo");

      when(intervinienteService.porId(anyLong())).thenReturn(Optional.of(interviniente));
      when(intervinienteService.guardarEditar(any(Interviniente.class))).thenReturn(interviniente);

      mockMvc.perform(put("/1")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(new ObjectMapper().writeValueAsString(intervinienteDTO)))
              .andExpect(status().isBadRequest());
   }

    @Test
     void testEliminar() throws Exception {
        when(intervinienteService.porId(anyLong())).thenReturn(Optional.of(interviniente));

        mockMvc.perform(delete("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

   @Test
   void testEliminarNotFound() throws Exception {
      when(intervinienteService.porId(10L)).thenReturn(Optional.empty());

      mockMvc.perform(delete("/10")
                      .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isNotFound());
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

    @Test
    void testAsignarInterviniente() throws Exception {
        IntervinienteDTO intervinienteDTO = new IntervinienteDTO();
        intervinienteDTO.setId(1L);
        intervinienteDTO.setNombre("Nombre");
        intervinienteDTO.setTipoIntervencion("Tipo");

        when(intervinienteService.porId(anyLong())).thenReturn(Optional.of(interviniente));
        when(intervinienteService.guardarEditar(any(Interviniente.class))).thenReturn(interviniente);

        mockMvc.perform(put("/asignar-int/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(intervinienteDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAsignarInterviniente_ValidationError() throws Exception {
        IntervinienteDTO intervinienteDTO = new IntervinienteDTO();
        intervinienteDTO.setId(1L);
        intervinienteDTO.setNombre(""); // Invalid name to trigger validation error
        intervinienteDTO.setTipoIntervencion("Tipo");

        mockMvc.perform(put("/asignar-int/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(intervinienteDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAsignarInterviniente_NotFound() throws Exception {
        IntervinienteDTO intervinienteDTO = new IntervinienteDTO();
        intervinienteDTO.setId(1L);
        intervinienteDTO.setNombre("Nombre");
        intervinienteDTO.setTipoIntervencion("Tipo");

        when(intervinienteService.porId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/asignar-int/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(intervinienteDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerIntervinientesPorProcedimiento() throws Exception {
        Long procedimientoId = 1L;
        List<Interviniente> intervinientes = new ArrayList<>();
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);
        interviniente.setNombre("Nombre");
        interviniente.setTipoIntervencion("Tipo");
        intervinientes.add(interviniente);

        when(intervinienteService.listarPorProc(procedimientoId)).thenReturn(intervinientes);

        mockMvc.perform(get("/intervinientes-por-procedimiento/" + procedimientoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is(interviniente.getNombre())));
    }

    @Test
    void testActualizacion() throws Exception {
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);
        interviniente.setNombre("Nombre");
        interviniente.setTipoIntervencion("Tipo");

        when(intervinienteService.guardarEditar(any(Interviniente.class))).thenReturn(interviniente);

        mockMvc.perform(put("/actualizacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(interviniente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(interviniente.getNombre())));
    }

    @Test
    void testManejarExcepcionDeJsonIncorrecto() throws Exception {
        mockMvc.perform(put("/actualizacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }
}