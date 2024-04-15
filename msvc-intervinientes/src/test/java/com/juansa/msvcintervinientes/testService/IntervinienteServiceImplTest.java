package com.juansa.msvcintervinientes.testService;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.models.entity.Interviniente;
import com.juansa.msvcintervinientes.exception.IntervinienteNoEncontradoException;
import com.juansa.msvcintervinientes.repositories.IntervinienteRepository;
import com.juansa.msvcintervinientes.services.IntervinienteServiceImpl;
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
class IntervinienteServiceImplTest {

    @Mock
    private IntervinienteRepository repositorio;

    private IntervinienteServiceImpl servicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servicio = new IntervinienteServiceImpl(repositorio, new ModelMapper());
    }

    @Test
    void testListar() {
        // Crear una lista de Intervinientes
        List<Interviniente> expected = Arrays.asList(new Interviniente(), new Interviniente());

        // Simular el comportamiento del método findAll() del repositorio
        when(repositorio.findAll()).thenReturn(expected);

        // Llamar al método listar() del servicio
        List<Interviniente> actual = servicio.listar();

        // Verificar que el método listar() devuelve la lista esperada
        assertEquals(expected, actual);
    }

    @Test
    void testPorId() {
        // Crear un Interviniente
        Interviniente expected = new Interviniente();
        expected.setId(1L);

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(expected));

        // Llamar al método porId() del servicio
        Optional<Interviniente> actual = servicio.porId(1L);

        // Verificar que el método porId() devuelve el Interviniente esperado
        assertEquals(expected, actual.orElse(null));
    }

    @Test
    void testGuardarNuevo() {
        IntervinienteDTO dto = new IntervinienteDTO();
        dto.setNombre("Test");
        dto.setTipoIntervencion("Test Tipo Intervencion");

        Interviniente interviniente = new Interviniente();
        interviniente.setNombre(dto.getNombre());
        interviniente.setTipoIntervencion(dto.getTipoIntervencion());

        when(repositorio.save(any(Interviniente.class))).thenReturn(interviniente);

        Interviniente result = servicio.guardarNuevo(dto);

        assertNotNull(result);
        assertEquals(dto.getNombre(), result.getNombre());
        assertEquals(dto.getTipoIntervencion(), result.getTipoIntervencion());
    }

    @Test
    void testGuardarEditar() {
        // Crear un Interviniente
        Interviniente expected = new Interviniente();
        expected.setId(1L);
        expected.setNombre("Test Editado");
        expected.setTipoIntervencion("Test Tipo Intervencion Editado");

        // Simular el comportamiento del método findById() del repositorio
        when(repositorio.findById(1L)).thenReturn(Optional.of(new Interviniente()));

        // Simular el comportamiento del método save() del repositorio
        when(repositorio.save(any(Interviniente.class))).thenReturn(expected);

        // Llamar al método guardarEditar() del servicio
        Interviniente actual = servicio.guardarEditar(expected);

        // Verificar que el método guardarEditar() devuelve el Interviniente esperado
        assertEquals(expected, actual);
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
    void testGuardarEditarThrowsException() {
        // Crear un Interviniente
        Interviniente interviniente = new Interviniente();
        interviniente.setId(1L);

        // Simular el comportamiento del método findById() del repositorio para devolver un Optional vacío
        when(repositorio.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método guardarEditar() del servicio y verificar que lanza la excepción IntervinienteNoEncontradoException
        assertThrows(IntervinienteNoEncontradoException.class, () -> servicio.guardarEditar(interviniente));
    }
}
