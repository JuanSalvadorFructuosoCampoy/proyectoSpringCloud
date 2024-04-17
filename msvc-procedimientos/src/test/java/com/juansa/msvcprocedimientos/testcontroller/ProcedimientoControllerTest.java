package com.juansa.msvcprocedimientos.testcontroller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import com.juansa.msvcprocedimientos.models.Interviniente;
import com.juansa.msvcprocedimientos.models.entity.Procedimiento;
import com.juansa.msvcprocedimientos.exception.NumeroDuplicadoException;
import com.juansa.msvcprocedimientos.services.ProcedimientoService;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class ProcedimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcedimientoService procedimientoService;

    @MockBean
    private Procedimiento procedimiento;

    @BeforeEach
     void setup() {
        procedimiento = new Procedimiento();
        procedimiento.setId(1L);
        procedimiento.setNumeroProcedimiento("PR0001");
        procedimiento.setAnno(2024);
    }

    @Test
    void testPorId() throws Exception {
        Long procedimientoId = 1L;
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(procedimientoId);
        procedimiento.setNumeroProcedimiento("PR0001");

        when(procedimientoService.porIdConIntervinientes(procedimientoId)).thenReturn(Optional.of(procedimiento));

        mockMvc.perform(get("/" + procedimientoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(procedimiento.getId().intValue())))
                .andExpect(jsonPath("$.numeroProcedimiento", is(procedimiento.getNumeroProcedimiento())));

        verify(procedimientoService, times(1)).porIdConIntervinientes(procedimientoId);
    }

    @Test
     void testListar() throws Exception {
        when(procedimientoService.listar()).thenReturn(Arrays.asList(procedimiento));

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
     void testCrear() throws Exception {
        ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();
        procedimientoDTO.setNumeroProcedimiento("PR0001");
        procedimientoDTO.setAnno(2024);

        when(procedimientoService.guardarNuevo(any(ProcedimientoDTO.class))).thenReturn(procedimiento);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(procedimientoDTO)))
                .andExpect(status().isCreated());
    }

   @Test
   void testCrearError() throws Exception {
      ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();
      procedimientoDTO.setNumeroProcedimiento("");
      procedimientoDTO.setAnno(2024);

      when(procedimientoService.guardarNuevo(any(ProcedimientoDTO.class))).thenReturn(procedimiento);

      mockMvc.perform(post("/")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(new ObjectMapper().writeValueAsString(procedimientoDTO)))
              .andExpect(status().isBadRequest());
   }

    @Test
     void testEditar() throws Exception {
        ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();
        procedimientoDTO.setNumeroProcedimiento("PR0001");
        procedimientoDTO.setAnno(2024);

        when(procedimientoService.porId(anyLong())).thenReturn(Optional.of(procedimiento));
        when(procedimientoService.guardarEditar(any(Procedimiento.class))).thenReturn(procedimiento);

        mockMvc.perform(put("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(procedimientoDTO)))
                .andExpect(status().isCreated());
    }

   @Test
   void testEditarNotFound() throws Exception {
      ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();
      procedimientoDTO.setNumeroProcedimiento("PR0001");
      procedimientoDTO.setAnno(2024);

      when(procedimientoService.porId(10L)).thenReturn(Optional.empty());
      when(procedimientoService.guardarEditar(any(Procedimiento.class))).thenReturn(procedimiento);

      mockMvc.perform(put("/10")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(new ObjectMapper().writeValueAsString(procedimientoDTO)))
              .andExpect(status().isNotFound());
   }

    @Test
    void testEditarThrowsNumeroDuplicadoException() throws Exception {
        // Crear un ProcedimientoDTO
        ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();
        procedimientoDTO.setNumeroProcedimiento("PR0001");

        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);
        procedimiento.setNumeroProcedimiento("PR0002");

        // Crear otro Procedimiento con el mismo número
        Procedimiento procedimientoDuplicado = new Procedimiento();
        procedimientoDuplicado.setId(2L);
        procedimientoDuplicado.setNumeroProcedimiento("PR0001");

        // Configurar el mock del procedimientoService para que devuelva el Procedimiento cuando se llame al método porId()
        when(procedimientoService.porId(1L)).thenReturn(Optional.of(procedimiento));

        // Configurar el mock del procedimientoService para que devuelva el Procedimiento duplicado cuando se llame al método porNumero()
        when(procedimientoService.porNumero("PR0001")).thenReturn(Optional.of(procedimientoDuplicado));

        // Llamar al método editar() del controlador y verificar que lanza la excepción NumeroDuplicadoException
        mockMvc.perform(put("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(procedimientoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NumeroDuplicadoException))
                .andExpect(result -> assertEquals("Ya existe un procedimiento con ese número.", result.getResolvedException().getMessage()));
    }

   @Test
   void testEditarError() throws Exception {
      ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();
      procedimientoDTO.setNumeroProcedimiento("");
      procedimientoDTO.setAnno(2024);

      when(procedimientoService.porId(anyLong())).thenReturn(Optional.of(procedimiento));
      when(procedimientoService.guardarEditar(any(Procedimiento.class))).thenReturn(procedimiento);

      mockMvc.perform(put("/1")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(new ObjectMapper().writeValueAsString(procedimientoDTO)))
              .andExpect(status().isBadRequest());
   }

    @Test
     void testEliminar() throws Exception {
        when(procedimientoService.porId(anyLong())).thenReturn(Optional.of(procedimiento));

        mockMvc.perform(delete("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

   @Test
   void testEliminarNotFound() throws Exception {
      when(procedimientoService.porId(10L)).thenReturn(Optional.empty());

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
    void testExceptionMessage() {
        NumeroDuplicadoException exception = new NumeroDuplicadoException();
        assertEquals("Ya existe un procedimiento con ese número.", exception.getMessage());
    }

    @Test
    void testManejarExcepcion() throws Exception {
        // Crear un ProcedimientoDTO
        ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();
        procedimientoDTO.setNumeroProcedimiento("PR0001");

        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);

        //Configurar los mocks
        when(procedimientoService.porId(1L)).thenReturn(Optional.of(procedimiento));
        when(procedimientoService.guardarEditar(any(Procedimiento.class))).thenThrow(new NumeroDuplicadoException());

        // Llamar al método manejarExcepcion() del controlador
        mockMvc.perform(put("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(procedimientoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NumeroDuplicadoException))
                .andExpect(result -> assertEquals("Ya existe un procedimiento con ese número.", result.getResolvedException().getMessage()));
    }

    @Test
    void testAsignarInterviniente() throws Exception {
        Long procedimientoId = 1L;
        Long interId = 1L;
        Interviniente interviniente = new Interviniente();
        interviniente.setId(interId);

        when(procedimientoService.obtenerInterviniente(interId)).thenReturn(Optional.of(interviniente));
        when(procedimientoService.asignarInterviniente(interviniente, procedimientoId)).thenReturn(Optional.of(interviniente));

        mockMvc.perform(put("/asignar-int/" + procedimientoId)
                        .param("interId", String.valueOf(interId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(interviniente.getId().intValue())));

        verify(procedimientoService, times(1)).obtenerInterviniente(interId);
        verify(procedimientoService, times(1)).asignarInterviniente(interviniente, procedimientoId);
    }

    @Test
    void testAsignarIntervinienteOptEmpty() throws Exception {
        Long procedimientoId = 1L;
        Long interId = 1L;

        when(procedimientoService.obtenerInterviniente(interId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/asignar-int/" + procedimientoId)
                        .param("interId", String.valueOf(interId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("No existe el interviniente indicado.")));

        verify(procedimientoService, times(1)).obtenerInterviniente(interId);
    }

    @Test
    void testAsignarIntervinienteException() throws Exception {
        Long procedimientoId = 1L;
        Long interId = 1L;
        Interviniente interviniente = new Interviniente();
        interviniente.setId(interId);

        when(procedimientoService.obtenerInterviniente(interId)).thenReturn(Optional.of(interviniente));
        when(procedimientoService.asignarInterviniente(interviniente, procedimientoId)).thenThrow(new RuntimeException("Error en la comunicación"));

        mockMvc.perform(put("/asignar-int/" + procedimientoId)
                        .param("interId", String.valueOf(interId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("No existe el interviniente por el id o error en la comunicación: Error en la comunicación")));

        verify(procedimientoService, times(1)).obtenerInterviniente(interId);
        verify(procedimientoService, times(1)).asignarInterviniente(interviniente, procedimientoId);
    }

    @Test
    void testAsignarIntervinienteNotFound() throws Exception {
        Long procedimientoId = 1L;
        Long interId = 1L;

        when(procedimientoService.obtenerInterviniente(interId)).thenReturn(Optional.of(new Interviniente()));
        when(procedimientoService.asignarInterviniente(any(Interviniente.class), eq(procedimientoId))).thenReturn(Optional.empty());

        mockMvc.perform(put("/asignar-int/" + procedimientoId)
                        .param("interId", String.valueOf(interId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(procedimientoService, times(1)).obtenerInterviniente(interId);
        verify(procedimientoService, times(1)).asignarInterviniente(any(Interviniente.class), eq(procedimientoId));
    }

    @Test
    void testCrearIntervinienteCatch() throws Exception {
        Long procedimientoId = 1L;
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        when(procedimientoService.crearInterviniente(interviniente, procedimientoId)).thenThrow(new FeignException.BadRequest("Error en la comunicación", Request.create(Request.HttpMethod.GET, "Dummy", new HashMap<>(), Request.Body.empty(), new RequestTemplate()), null, null));

        mockMvc.perform(post("/crear-int/" + procedimientoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(interviniente)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("No se pudo crear el interviniente o error en la comunicación: Error en la comunicación")));

        verify(procedimientoService, times(1)).crearInterviniente(interviniente, procedimientoId);
    }

    @Test
    void testCrearIntervinienteOptPresent() throws Exception {
        Long procedimientoId = 1L;
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        when(procedimientoService.crearInterviniente(interviniente, procedimientoId)).thenReturn(Optional.of(interviniente));

        mockMvc.perform(post("/crear-int/" + procedimientoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(interviniente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(interviniente.getId().intValue())));

        verify(procedimientoService, times(1)).crearInterviniente(interviniente, procedimientoId);
    }

    @Test
    void testCrearIntervinienteNotFound() throws Exception {
        Long procedimientoId = 1L;
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        when(procedimientoService.crearInterviniente(interviniente, procedimientoId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/crear-int/" + procedimientoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(interviniente)))
                .andExpect(status().isNotFound());

        verify(procedimientoService, times(1)).crearInterviniente(interviniente, procedimientoId);
    }

    @Test
    void testEliminarIntervinienteOptEmpty() throws Exception {
        Long procedimientoId = 1L;
        Long interId = 1L;

        when(procedimientoService.obtenerInterviniente(interId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/eliminar-int/" + procedimientoId)
                        .param("interId", String.valueOf(interId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(procedimientoService, times(1)).obtenerInterviniente(interId);
    }

    @Test
    void testEliminarIntervinienteException() throws Exception {
        Long procedimientoId = 1L;
        Long interId = 1L;
        Interviniente interviniente = new Interviniente();
        interviniente.setId(interId);

        when(procedimientoService.obtenerInterviniente(interId)).thenReturn(Optional.of(interviniente));
        when(procedimientoService.eliminarInterviniente(interviniente, procedimientoId)).thenThrow(new RuntimeException("Error en la comunicación"));

        mockMvc.perform(delete("/eliminar-int/" + procedimientoId)
                        .param("interId", String.valueOf(interId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(procedimientoService, times(1)).obtenerInterviniente(interId);
        verify(procedimientoService, times(1)).eliminarInterviniente(interviniente, procedimientoId);
    }

    @Test
    void testEliminarIntervinienteSuccess() throws Exception {
        Long procedimientoId = 1L;
        Long interId = 1L;
        Interviniente interviniente = new Interviniente();
        interviniente.setId(interId);

        when(procedimientoService.obtenerInterviniente(interId)).thenReturn(Optional.of(interviniente));
        when(procedimientoService.eliminarInterviniente(interviniente, procedimientoId)).thenReturn(Optional.of(interviniente));

        mockMvc.perform(delete("/eliminar-int/" + procedimientoId)
                        .param("interId", String.valueOf(interId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(procedimientoService, times(1)).obtenerInterviniente(interId);
        verify(procedimientoService, times(1)).eliminarInterviniente(interviniente, procedimientoId);
    }

    @Test
    void testEliminarIntervinienteNotFound() throws Exception {
        Long procedimientoId = 1L;
        Long interId = 1L;
        Interviniente interviniente = new Interviniente();
        interviniente.setId(interId);

        when(procedimientoService.obtenerInterviniente(interId)).thenReturn(Optional.of(interviniente));
        when(procedimientoService.eliminarInterviniente(interviniente, procedimientoId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/eliminar-int/" + procedimientoId)
                        .param("interId", String.valueOf(interId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(procedimientoService, times(1)).obtenerInterviniente(interId);
        verify(procedimientoService, times(1)).eliminarInterviniente(interviniente, procedimientoId);
    }

    @Test
    void testManejarExcepcionDeJsonIncorrecto() throws Exception {
        String jsonIncorrecto = "{malformed json}";

        mockMvc.perform(post("/crear-int/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonIncorrecto))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("El JSON proporcionado es incorrecto: JSON parse error: Unexpected character ('m' (code 109)): was expecting double-quote to start field name")));
    }
    }

