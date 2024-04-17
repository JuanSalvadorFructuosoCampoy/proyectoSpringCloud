package com.juansa.msvcprocedimientos.testService;

import com.juansa.msvcprocedimientos.clients.IntervinienteClientRest;
import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import com.juansa.msvcprocedimientos.models.Interviniente;
import com.juansa.msvcprocedimientos.models.entity.Procedimiento;
import com.juansa.msvcprocedimientos.exception.NumeroDuplicadoException;
import com.juansa.msvcprocedimientos.exception.ProcedimientoNoEncontradoException;
import com.juansa.msvcprocedimientos.repositories.ProcedimientoRepository;
import com.juansa.msvcprocedimientos.services.ProcedimientoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProcedimientoServiceImplTest {

    @Mock
    private ProcedimientoRepository repositorio;

    private ProcedimientoServiceImpl servicio;

    private IntervinienteClientRest cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = mock(IntervinienteClientRest.class);
        servicio = new ProcedimientoServiceImpl(repositorio, new ModelMapper(), cliente);
    }

    @Test
    void testListar() {
        // Crear una lista de Procedimientos
        List<Procedimiento> expected = Arrays.asList(new Procedimiento(), new Procedimiento());

        // Simular el comportamiento del método findAll() del repositorio
        when(repositorio.findAll()).thenReturn(expected);

        // Llamar al método listar() del servicio
        List<Procedimiento> actual = servicio.listar();

        // Verificar que el método listar() devuelve la lista esperada
        assertEquals(expected, actual);
    }

    @Test
    void testPorId() {
        // Crear un Procedimiento
        Procedimiento expected = new Procedimiento();
        expected.setId(1L);

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(expected));

        // Llamar al método porId() del servicio
        Optional<Procedimiento> actual = servicio.porId(1L);

        // Verificar que el método porId() devuelve el Procedimiento esperado
        assertEquals(expected, actual.orElse(null));
    }

    @Test
    void testPorIdConIntervinientes() {
        Long procedimientoId = 1L;
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(procedimientoId);

        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);
        interviniente.setProcedimientoId(procedimientoId);

        List<Interviniente> intervinientes = Arrays.asList(interviniente);

        when(repositorio.findById(procedimientoId)).thenReturn(Optional.of(procedimiento));
        when(cliente.obtenerIntervinientesPorProcedimiento(procedimientoId)).thenReturn(intervinientes);

        Optional<Procedimiento> result = servicio.porIdConIntervinientes(procedimientoId);

        assertTrue(result.isPresent());
        assertEquals(procedimientoId, result.get().getId());
        assertEquals(intervinientes, result.get().getIntervinientes());

        verify(repositorio, times(1)).findById(procedimientoId);
        verify(cliente, times(1)).obtenerIntervinientesPorProcedimiento(procedimientoId);
    }

    @Test
    void testPorIdConIntervinientesReturnsEmptyOptional() {
        Long procedimientoId = 1L;

        when(repositorio.findById(procedimientoId)).thenReturn(Optional.empty());

        Optional<Procedimiento> result = servicio.porIdConIntervinientes(procedimientoId);

        assertTrue(result.isEmpty());

        verify(repositorio, times(1)).findById(procedimientoId);
    }

    @Test
    void testPorNumero() {
        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setNumeroProcedimiento("PR0001");

        // Configurar el mock del repositorio para que devuelva el Procedimiento cuando se llame al método getProcedimientoPorNumero()
        when(repositorio.getProcedimientoPorNumero("PR0001")).thenReturn(Optional.of(procedimiento));

        // Llamar al método porNumero() del servicio
        Optional<Procedimiento> resultado = servicio.porNumero("PR0001");

        // Verificar que el método porNumero() devuelve el Procedimiento esperado
        assertTrue(resultado.isPresent());
        assertEquals("PR0001", resultado.get().getNumeroProcedimiento());
    }

    @Test
    void testGuardarNuevo() {
        ProcedimientoDTO dto = new ProcedimientoDTO();
        dto.setNumeroProcedimiento("PR0001");
        dto.setAnno(2024);

        Procedimiento interviniente = new Procedimiento();
        interviniente.setNumeroProcedimiento(dto.getNumeroProcedimiento());
        interviniente.setAnno(dto.getAnno());

        when(repositorio.save(any(Procedimiento.class))).thenReturn(interviniente);

        Procedimiento result = servicio.guardarNuevo(dto);

        assertNotNull(result);
        assertEquals(dto.getNumeroProcedimiento(), result.getNumeroProcedimiento());
        assertEquals(dto.getAnno(), result.getAnno());
    }

    @Test
    void testGuardarNuevoThrowsNumeroDuplicadoException() {
        // Crear un ProcedimientoDTO
        ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();
        procedimientoDTO.setNumeroProcedimiento("PR0001");

        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setNumeroProcedimiento("PR0001");

        // Configurar el mock del repositorio para que devuelva el Procedimiento cuando se llame al método getProcedimientoPorNumero()
        when(repositorio.getProcedimientoPorNumero("PR0001")).thenReturn(Optional.of(procedimiento));

        // Llamar al método guardarNuevo() del servicio y verificar que lanza la excepción NumeroDuplicadoException
        assertThrows(NumeroDuplicadoException.class, () -> servicio.guardarNuevo(procedimientoDTO));
    }

    @Test
    void testGuardarEditar() {
        // Crear un Procedimiento
        Procedimiento expected = new Procedimiento();
        expected.setId(1L);
        expected.setNumeroProcedimiento("Test Editado");
        expected.setAnno(2024);

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(new Procedimiento()));

        // Simular el comportamiento del método save() del repositorio
        when(repositorio.save(any(Procedimiento.class))).thenReturn(expected);

        // Llamar al método guardarEditar() del servicio
        Procedimiento actual = servicio.guardarEditar(expected);

        // Verificar que el método guardarEditar() devuelve el Procedimiento esperado
        assertEquals(expected, actual);
    }

    @Test
    void testGuardarEditarThrowsNumeroDuplicadoException() {
        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);
        procedimiento.setNumeroProcedimiento("PR0001");

        // Crear otro Procedimiento con el mismo número
        Procedimiento procedimientoDuplicado = new Procedimiento();
        procedimientoDuplicado.setId(2L);
        procedimientoDuplicado.setNumeroProcedimiento("PR0001");

        // Configurar el mock del repositorio para que devuelva el Procedimiento cuando se llame al método findById()
        when(repositorio.findById(1L)).thenReturn(Optional.of(procedimiento));

        // Configurar el mock del repositorio para que devuelva el Procedimiento duplicado cuando se llame al método getProcedimientoPorNumero()
        when(repositorio.getProcedimientoPorNumero("PR0001")).thenReturn(Optional.of(procedimientoDuplicado));

        // Llamar al método guardarEditar() del servicio y verificar que lanza la excepción NumeroDuplicadoException
        assertThrows(NumeroDuplicadoException.class, () -> servicio.guardarEditar(procedimiento));
    }

    @Test
    void testEliminar() {
        // Simular el comportamiento del método deleteById() del repositorio
        doNothing().when(repositorio).deleteById(1L);

        // Llamar al método eliminar() del servicio
        servicio.eliminar(1L);

        // Verificar que el método eliminar() no lanza ninguna excepción
        verify(repositorio, times(1)).deleteById(1L);
    }

    @Test
    void testObtenerInterviniente() {
        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        // Simular el comportamiento del método porId() del cliente
        when(cliente.porId(1L)).thenReturn(Optional.of(interviniente));

        // Llamar al método obtenerInterviniente() del servicio
        Optional<Interviniente> actual = servicio.obtenerInterviniente(1L);

        // Verificar que el método obtenerInterviniente() devuelve el Interviniente esperado
        assertTrue(actual.isPresent());
        assertEquals(interviniente, actual.get());
    }

    @Test
    void testAsignarInterviniente() {
        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);

        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(procedimiento));

        // Simular el comportamiento del método porId() del cliente
        when(cliente.porId(1L)).thenReturn(Optional.of(interviniente));

        // Llamar al método asignarInterviniente() del servicio
        Optional<Interviniente> actual = servicio.asignarInterviniente(interviniente, 1L);

        // Verificar que el método asignarInterviniente() devuelve el Interviniente esperado
        assertTrue(actual.isPresent());
        assertEquals(interviniente, actual.get());
    }

    @Test
    void testAsignarIntervinienteReturnsEmptyOptional() {
        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método asignarInterviniente() del servicio
        Optional<Interviniente> actual = servicio.asignarInterviniente(new Interviniente(), 1L);

        // Verificar que el método asignarInterviniente() devuelve un Optional vacío
        assertTrue(actual.isEmpty());
    }

    @Test
    void testAsignarIntervinienteProcedimientoEmptyOptional() {
        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);

        Interviniente interviniente = null;
        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(procedimiento));

        // Llamar al método asignarInterviniente() del servicio
        Optional<Interviniente> actual = servicio.asignarInterviniente(new Interviniente(), 1L);

        when(cliente.porId(1L)).thenReturn(Optional.empty());
        // Verificar que el método asignarInterviniente() devuelve un Optional vacío
        assertTrue(actual.isEmpty());
    }

    @Test
    void testCrearIntervinienteProcedimientoExistente() {
        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);

        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(procedimiento));

        // Simular el comportamiento del método crear() del cliente
        when(cliente.crear(interviniente)).thenReturn(interviniente);

        // Llamar al método crearInterviniente() del servicio
        Optional<Interviniente> actual = servicio.crearInterviniente(interviniente, 1L);

        // Verificar que el método crearInterviniente() devuelve el Interviniente esperado
        assertTrue(actual.isPresent());
        assertEquals(interviniente, actual.get());
    }

    @Test
    void testCrearIntervinienteProcedimientoNoExistente() {
        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        // Simular el comportamiento del método findById() del repositorio para devolver un Optional vacío
        when(repositorio.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método crearInterviniente() del servicio
        Optional<Interviniente> actual = servicio.crearInterviniente(interviniente, 1L);

        // Verificar que el método crearInterviniente() devuelve un Optional vacío
        assertTrue(actual.isEmpty());
    }

    @Test
    void testEliminarIntervinienteExistenteAsociado() {
        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);

        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);
        interviniente.setProcedimientoId(1L);

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(procedimiento));

        // Simular el comportamiento del método porId() del cliente
        when(cliente.porId(1L)).thenReturn(Optional.of(interviniente));

        // Llamar al método eliminarInterviniente() del servicio
        Optional<Interviniente> actual = servicio.eliminarInterviniente(interviniente, 1L);

        // Verificar que el método eliminarInterviniente() devuelve el Interviniente esperado
        assertTrue(actual.isPresent());
        assertEquals(interviniente, actual.get());
    }

    @Test
    void testEliminarIntervinienteProcedimientoNoExistente() {
        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        // Simular el comportamiento del método findById() del repositorio para devolver un Optional vacío
        when(repositorio.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método eliminarInterviniente() del servicio
        Optional<Interviniente> actual = servicio.eliminarInterviniente(interviniente, 1L);

        // Verificar que el método eliminarInterviniente() devuelve un Optional vacío
        assertTrue(actual.isEmpty());
    }

    @Test
    void testEliminarIntervinienteNoExistente() {
        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);

        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(procedimiento));

        // Simular el comportamiento del método porId() del cliente para devolver un Optional vacío
        when(cliente.porId(1L)).thenReturn(Optional.empty());

        // Llamar al método eliminarInterviniente() del servicio
        Optional<Interviniente> actual = servicio.eliminarInterviniente(interviniente, 1L);

        // Verificar que el método eliminarInterviniente() devuelve un Optional vacío
        assertTrue(actual.isEmpty());
    }

    @Test
    void testEliminarIntervinienteNoAsociado() {
        // Crear un Procedimiento
        Procedimiento procedimiento = new Procedimiento();
        procedimiento.setId(1L);

        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);
        interviniente.setProcedimientoId(2L);

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(procedimiento));

        // Simular el comportamiento del método porId() del cliente
        when(cliente.porId(1L)).thenReturn(Optional.of(interviniente));

        // Llamar al método eliminarInterviniente() del servicio
        Optional<Interviniente> actual = servicio.eliminarInterviniente(interviniente, 1L);

        // Verificar que el método eliminarInterviniente() devuelve un Optional vacío
        assertTrue(actual.isEmpty());
    }

    @Test
    void testGuardarEditarThrowsException() {
        // Crear un Procedimiento
        Procedimiento interviniente = new Procedimiento();
        interviniente.setId(1L);

        // Simular el comportamiento del método findById() del repositorio para devolver un Optional vacío
        when(repositorio.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método guardarEditar() del servicio y verificar que lanza la excepción ProcedimientoNoEncontradoException
        assertThrows(ProcedimientoNoEncontradoException.class, () -> servicio.guardarEditar(interviniente));
    }
}
